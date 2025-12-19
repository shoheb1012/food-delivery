package com.example.repository;


import com.example.entity.LocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationHistoryRepository extends JpaRepository<LocationHistory, Long> {
    List<LocationHistory> findByDeliveryBoyIdOrderByRecordedAtDesc(Long deliveryBoyId);
    List<LocationHistory> findByOrderIdOrderByRecordedAtDesc(Long orderId);
}