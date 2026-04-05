package com.homemitra.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ServiceResponse {
    private Long id;
    private String name;
    private String slug;
    private String shortDesc;
    private String description;
    private BigDecimal basePrice;
    private int durationMins;
    private String imageUrl;
    private BigDecimal ratingAvg;
    private int totalBookings;
    private boolean featured;
    private String categoryName;
    private String categoryIcon;
}
