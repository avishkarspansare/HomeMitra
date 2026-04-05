package com.homemitra.repository;

import com.homemitra.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByServiceIdOrderByCreatedAtDesc(Long serviceId);
    List<Review> findByProviderIdOrderByCreatedAtDesc(Long providerId);
    boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.service.id = :serviceId")
    Double avgRatingForService(Long serviceId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider.id = :providerId")
    Double avgRatingForProvider(Long providerId);
}
