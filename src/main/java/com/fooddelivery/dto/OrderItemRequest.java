package com.fooddelivery.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long foodItemId;
    private Integer quantity;
}
