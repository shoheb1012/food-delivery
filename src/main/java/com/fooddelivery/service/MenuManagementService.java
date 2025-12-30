package com.fooddelivery.service;

import com.fooddelivery.dto.MenuItemRequest;
import com.fooddelivery.entity.FoodItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.FoodItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuManagementService {
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    @Transactional
    public FoodItem addMenuItem(Long restaurantId, MenuItemRequest req) {
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow();

        FoodItem item = new FoodItem();
        item.setRestaurant(r);
        item.setName(req.getName());
        item.setDescription(req.getDescription());
        item.setPrice(req.getPrice());
        item.setIsAvailable(true);

        return foodItemRepository.save(item);
    }

    @Transactional
    public void updatePrice(Long itemId, Double price) {
        FoodItem item = foodItemRepository.findById(itemId).orElseThrow();
        item.setPrice(price);
    }

    @Transactional
    public void removeItem(Long itemId) {
        FoodItem item = foodItemRepository.findById(itemId).orElseThrow();
        item.setIsAvailable(false);
    }

}
