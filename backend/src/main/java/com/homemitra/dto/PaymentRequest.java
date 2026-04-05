package com.homemitra.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
