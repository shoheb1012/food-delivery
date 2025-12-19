package com.example.repository;



import com.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByDeliveryBoyId(Long deliveryBoyId);
    List<Order> findByRestaurantId(Long restaurantId);
}
