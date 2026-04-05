package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="booking_id") private Booking booking;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="customer_id") private User customer;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="provider_id") private ProviderProfile provider;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="service_id") private Service service;
    @Column(nullable=false) private int rating;
    @Column(columnDefinition="TEXT") private String comment;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
}
