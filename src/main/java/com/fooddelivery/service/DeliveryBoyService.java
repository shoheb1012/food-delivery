package com.fooddelivery.service;
import com.fooddelivery.entity.DeliveryBoy;
import com.fooddelivery.entity.LocationHistory;
import com.fooddelivery.repository.DeliveryBoyRepository;
import com.fooddelivery.repository.LocationHistoryRepository;
import com.fooddelivery.util.LocationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryBoyService {


    private final DeliveryBoyRepository deliveryBoyRepository;
    private final LocationHistoryRepository locationHistoryRepository;

    /**
     * Update delivery boy location (called every 15 seconds from Android app)
     */
    @Transactional
    public void updateLocation(Long deliveryBoyId, Double latitude, Double longitude) {
        DeliveryBoy deliveryBoy = deliveryBoyRepository.findById(deliveryBoyId)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));

        deliveryBoy.setCurrentLatitude(latitude);
        deliveryBoy.setCurrentLongitude(longitude);
        deliveryBoy.setLastLocationUpdate(LocalDateTime.now());

        deliveryBoyRepository.save(deliveryBoy);

        // Save to location history
        LocationHistory history = new LocationHistory();
        history.setDeliveryBoy(deliveryBoy);
        history.setLatitude(latitude);
        history.setLongitude(longitude);
        history.setRecordedAt(LocalDateTime.now());

        locationHistoryRepository.save(history);

        System.out.println("Location updated for delivery boy: " + deliveryBoy.getName() +
                " at " + latitude + ", " + longitude);
    }

    /**
     * Find nearest available delivery boy to restaurant
     */
    public DeliveryBoy findNearestDeliveryBoy(Double restaurantLat, Double restaurantLon) {
        List<DeliveryBoy> availableBoys = deliveryBoyRepository.findByIsAvailableTrue();

        if (availableBoys.isEmpty()) {
            throw new RuntimeException("No delivery boys available");
        }

        DeliveryBoy nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (DeliveryBoy boy : availableBoys) {
            if (boy.getCurrentLatitude() != null && boy.getCurrentLongitude() != null) {
                double distance = LocationUtils.calculateDistance(
                        restaurantLat, restaurantLon,
                        boy.getCurrentLatitude(), boy.getCurrentLongitude()
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = boy;
                }
            }
        }

        if (nearest == null) {
            throw new RuntimeException("No delivery boys with location available");
        }

        System.out.println("🎯 Nearest delivery boy: " + nearest.getName() +
                " (Distance: " + String.format("%.2f", minDistance) + " km)");

        return nearest;
    }

    /**
     * Get delivery boy current location
     */
    public DeliveryBoy getDeliveryBoyLocation(Long deliveryBoyId) {
        return deliveryBoyRepository.findById(deliveryBoyId)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));
    }

    /**
     * Mark delivery boy as available/unavailable
     */
    @Transactional
    public void updateAvailability(Long deliveryBoyId, Boolean isAvailable) {
        DeliveryBoy deliveryBoy = deliveryBoyRepository.findById(deliveryBoyId)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));

        deliveryBoy.setIsAvailable(isAvailable);
        deliveryBoyRepository.save(deliveryBoy);
    }
}
