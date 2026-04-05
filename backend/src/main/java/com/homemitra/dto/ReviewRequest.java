package com.homemitra.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull private Long bookingId;
    @Min(1) @Max(5) private int rating;
    private String comment;
}
