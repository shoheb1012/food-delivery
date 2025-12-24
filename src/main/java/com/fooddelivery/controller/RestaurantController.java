package com.fooddelivery.controller;

import com.fooddelivery.entity.FoodItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.FoodItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    /**
     * Get all active restaurants
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findByIsActiveTrue();
        return ResponseEntity.ok(restaurants);
    }

    /**
     * Get restaurant by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get menu items for a restaurant
     */
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<FoodItem>> getMenu(@PathVariable Long id) {
        List<FoodItem> menu = foodItemRepository.findByRestaurantIdAndIsAvailableTrue(id);
        return ResponseEntity.ok(menu);
    }
}
