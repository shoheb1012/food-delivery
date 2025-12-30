package com.fooddelivery.dto;

import lombok.Data;

@Data
public class DeliveryBoyRegisterRequest {
    private String name;
    private String phone;
    private String password;
    private String vehicleNumber;
}
