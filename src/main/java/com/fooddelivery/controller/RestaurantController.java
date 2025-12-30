package com.fooddelivery.controller;

import com.fooddelivery.dto.CustomerRegisterRequest;
import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.RestaurantRegisterRequest;
import com.fooddelivery.entity.FoodItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.FoodItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
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
    private final RestaurantService restaurantService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<Restaurant> register(@RequestBody RestaurantRegisterRequest request) {
        try {
            Restaurant saved = restaurantService.register(request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(null); // Optionally return error details
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Restaurant> login(@RequestBody LoginRequest request) {
        try {
            Restaurant r = restaurantService.login(request);
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE RESTAURANT DETAILS
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @RequestBody Restaurant updated) {

        return restaurantRepository.findById(id).map(r -> {
            r.setName(updated.getName());
            r.setAddress(updated.getAddress());
            r.setLatitude(updated.getLatitude());
            r.setLongitude(updated.getLongitude());
            r.setIsActive(updated.getIsActive());
            return ResponseEntity.ok(restaurantRepository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ADD MENU ITEM
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<FoodItem> addFoodItem(
            @PathVariable Long restaurantId,
            @RequestBody FoodItem foodItem) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        foodItem.setRestaurant(restaurant);
        return ResponseEntity.ok(foodItemRepository.save(foodItem));
    }

    // UPDATE FOOD ITEM PRICE / AVAILABILITY
    @PutMapping("/menu/{itemId}")
    public ResponseEntity<FoodItem> updateFoodItem(
            @PathVariable Long itemId,
            @RequestBody FoodItem updated) {

        return foodItemRepository.findById(itemId).map(item -> {
            item.setPrice(updated.getPrice());
            item.setIsAvailable(updated.getIsAvailable());
            item.setDescription(updated.getDescription());
            return ResponseEntity.ok(foodItemRepository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    // REMOVE FOOD ITEM
    @DeleteMapping("/menu/{itemId}")
    public ResponseEntity<?> deleteFoodItem(@PathVariable Long itemId) {
        foodItemRepository.deleteById(itemId);
        return ResponseEntity.ok("Item removed");
    }

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
