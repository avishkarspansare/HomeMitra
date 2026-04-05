package com.homemitra.repository;

import com.homemitra.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingRef(String bookingRef);
    Page<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
    Page<Booking> findByProviderIdOrderByCreatedAtDesc(Long providerId, Pageable pageable);
    List<Booking> findByCustomerIdAndStatus(Long customerId, Booking.Status status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :userId")
    long countByCustomerId(Long userId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'PENDING'")
    long countPending();
}
