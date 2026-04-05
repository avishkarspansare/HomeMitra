package com.homemitra.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class BookingResponse {
    private Long id;
    private String bookingRef;
    private String serviceName;
    private String serviceIcon;
    private String providerName;
    private String addressLine;
    private LocalDateTime scheduledAt;
    private BigDecimal finalAmount;
    private String status;
    private LocalDateTime createdAt;
}
