package com.fooddelivery.dto;

import lombok.Data;

@Data
public class RestaurantRegisterRequest {
    private String name;
    private String phone;
    private String password;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
}
