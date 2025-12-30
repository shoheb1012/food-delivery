package com.fooddelivery.dto;

import lombok.Data;

@Data
public class MenuItemRequest {
    private String name;
    private String description;
    private Double price;
}
