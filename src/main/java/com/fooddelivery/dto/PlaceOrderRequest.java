package com.fooddelivery.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PlaceOrderRequest {
    private Long customerId;
    private Long restaurantId;
    private List<OrderItemRequest> items;
    private String deliveryAddress;
    private Double customerLatitude;
    private Double customerLongitude;
}

