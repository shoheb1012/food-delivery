package com.fooddelivery.service;

import com.fooddelivery.dto.PlaceOrderRequest;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final DeliveryBoyService deliveryBoyService;

    @Transactional
    public Order placeOrder(PlaceOrderRequest request) {

        // 1. Get customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Get restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // 3. Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setCustomerLatitude(request.getCustomerLatitude());
        order.setCustomerLongitude(request.getCustomerLongitude());
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        // 4. Add order items and calculate total
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (var itemReq : request.getItems()) {

            FoodItem foodItem = foodItemRepository.findById(itemReq.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(foodItem.getPrice());

            orderItems.add(orderItem);
            totalAmount += foodItem.getPrice() * itemReq.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 5. Save order first
        order = orderRepository.save(order);

        // 6. Find and assign nearest delivery boy
        try {
            DeliveryBoy nearestBoy = deliveryBoyService.findNearestDeliveryBoy(
                    restaurant.getLatitude(),
                    restaurant.getLongitude()
            );

            order.setDeliveryBoy(nearestBoy);
            order.setStatus(OrderStatus.ASSIGNED);

            // Mark delivery boy as unavailable
            deliveryBoyService.updateAvailability(nearestBoy.getId(), false);

            System.out.println("✅ Order #" + order.getId()
                    + " assigned to " + nearestBoy.getName());

        } catch (Exception e) {
            System.out.println("⚠️ No delivery boy available, order placed but not assigned");
        }

        // 7. Update timestamp & save again
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }


    /**
     * Update order status
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        // If order delivered, make delivery boy available again
        if (newStatus == OrderStatus.DELIVERED && order.getDeliveryBoy() != null) {
            deliveryBoyService.updateAvailability(order.getDeliveryBoy().getId(), true);
        }

        return orderRepository.save(order);
    }

    /**
     * Get order details with delivery boy location
     */
    public Order getOrderWithLocation(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Get all orders for a customer
     */
    public List<Order> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * Get all orders for a restaurant
     */
    public List<Order> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
}