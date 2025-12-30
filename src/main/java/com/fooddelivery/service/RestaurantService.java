package com.fooddelivery.service;

import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.RestaurantRegisterRequest;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    @Transactional
    public Restaurant register(RestaurantRegisterRequest req) {
        Restaurant r = new Restaurant();
        r.setName(req.getName());
        r.setPhone(req.getPhone());
        r.setPassword(PasswordUtil.hash(req.getPassword()));
        r.setAddress(req.getAddress());
        r.setLatitude(req.getLatitude());
        r.setLongitude(req.getLongitude());
        r.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        return restaurantRepository.save(r);
    }

    public Restaurant login(LoginRequest req) {
        Restaurant r = restaurantRepository.findByPhone(req.getPhone())
                .orElseThrow(() -> new RuntimeException("Invalid phone"));

        if (!PasswordUtil.matches(req.getPassword(), r.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return r;
    }

    public List<Restaurant> getAllActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

}
