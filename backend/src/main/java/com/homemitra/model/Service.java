package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="services")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Service {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id") private ServiceCategory category;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String slug;
    @Column(name="short_desc") private String shortDesc;
    @Column(columnDefinition="TEXT") private String description;
    @Column(name="base_price", nullable=false) private BigDecimal basePrice;
    @Column(name="duration_mins") private int durationMins = 60;
    @Column(name="image_url") private String imageUrl;
    @Column(name="rating_avg") private BigDecimal ratingAvg = BigDecimal.ZERO;
    @Column(name="total_bookings") private int totalBookings = 0;
    @Column(name="is_featured") private boolean featured = false;
    @Column(name="is_active") private boolean active = true;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
}
