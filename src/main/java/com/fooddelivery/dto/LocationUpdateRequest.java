package com.fooddelivery.dto;



import lombok.Data;

@Data
public class LocationUpdateRequest {
    private Long deliveryBoyId;
    private Double latitude;
    private Double longitude;
}