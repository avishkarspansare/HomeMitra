package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="subscription_plans")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriptionPlan {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private BigDecimal price;
    @Column(nullable=false) private int duration;
    @Column(columnDefinition="JSON") private String features;
    @Column(name="discount_pct") private BigDecimal discountPct = BigDecimal.ZERO;
    @Column(name="is_active") private boolean active = true;
}
