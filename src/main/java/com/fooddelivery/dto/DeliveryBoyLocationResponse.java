package com.fooddelivery.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeliveryBoyLocationResponse {
    private Long deliveryBoyId;
    private String deliveryBoyName;
    private String phone;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdate;
    private String status;
}
