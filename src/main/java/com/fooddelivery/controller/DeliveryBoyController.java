package com.fooddelivery.controller;

import com.fooddelivery.dto.LocationUpdateRequest;
import com.fooddelivery.dto.DeliveryBoyLocationResponse;
import com.fooddelivery.entity.DeliveryBoy;
import com.fooddelivery.service.DeliveryBoyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-boy")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DeliveryBoyController {

    private final DeliveryBoyService deliveryBoyService;

    /**
     * Update delivery boy location (Called from Android app every 15 seconds)
     */
    @PostMapping("/update-location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest request) {
        try {
            deliveryBoyService.updateLocation(
                    request.getDeliveryBoyId(),
                    request.getLatitude(),
                    request.getLongitude()
            );

            return ResponseEntity.ok().body(
                    new ApiResponse(true, "Location updated successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, e.getMessage())
            );
        }
    }

    /**
     * Get delivery boy current location
     */
    @GetMapping("/{id}/location")
    public ResponseEntity<DeliveryBoyLocationResponse> getLocation(@PathVariable Long id) {
        try {
            DeliveryBoy deliveryBoy = deliveryBoyService.getDeliveryBoyLocation(id);

            DeliveryBoyLocationResponse response = new DeliveryBoyLocationResponse();
            response.setDeliveryBoyId(deliveryBoy.getId());
            response.setDeliveryBoyName(deliveryBoy.getName());
            response.setPhone(deliveryBoy.getPhone());
            response.setLatitude(deliveryBoy.getCurrentLatitude());
            response.setLongitude(deliveryBoy.getCurrentLongitude());
            response.setLastUpdate(deliveryBoy.getLastLocationUpdate());
            response.setStatus(deliveryBoy.getIsAvailable() ? "AVAILABLE" : "BUSY");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update availability status
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean isAvailable) {
        try {
            deliveryBoyService.updateAvailability(id, isAvailable);
            return ResponseEntity.ok(new ApiResponse(true, "Availability updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}

// Simple response wrapper
class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
