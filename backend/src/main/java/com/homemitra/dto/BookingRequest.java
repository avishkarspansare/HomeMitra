package com.homemitra.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull private Long serviceId;
    @NotNull private Long addressId;
    @NotNull @Future private LocalDateTime scheduledAt;
    private String notes;
}
