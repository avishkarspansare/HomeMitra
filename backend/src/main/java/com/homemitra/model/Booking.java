package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="bookings")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="booking_ref", nullable=false, unique=true) private String bookingRef;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="customer_id") private User customer;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="provider_id") private ProviderProfile provider;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="service_id") private Service service;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="address_id") private Address address;
    @Column(name="scheduled_at") private LocalDateTime scheduledAt;
    @Column(name="duration_mins") private int durationMins;
    @Column(nullable=false) private BigDecimal amount;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    @Column(name="final_amount", nullable=false) private BigDecimal finalAmount;
    @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
    @Column(columnDefinition="TEXT") private String notes;
    @Enumerated(EnumType.STRING) @Column(name="cancelled_by") private CancelledBy cancelledBy;
    @Column(name="cancel_reason", columnDefinition="TEXT") private String cancelReason;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name="updated_at") private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Status { PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED }
    public enum CancelledBy { CUSTOMER, PROVIDER, ADMIN }

    @PreUpdate public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
