package com.fooddelivery.controller;

import com.fooddelivery.dto.CustomerRegisterRequest;
import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<Customer> register(
            @RequestBody CustomerRegisterRequest request) {

        return ResponseEntity.ok(customerService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Customer> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(customerService.login(request));
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Customer> updateAddress(
            @PathVariable Long id,
            @RequestParam String address,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        return ResponseEntity.ok(
                customerService.updateAddress(id, address, latitude, longitude)
        );
    }
}
