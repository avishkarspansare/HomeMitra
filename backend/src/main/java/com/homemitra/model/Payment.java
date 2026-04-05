package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="payments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="booking_id") private Booking booking;
    @Column(name="razorpay_order_id") private String razorpayOrderId;
    @Column(name="razorpay_payment_id") private String razorpayPaymentId;
    @Column(name="razorpay_signature") private String razorpaySignature;
    @Column(nullable=false) private BigDecimal amount;
    private String currency = "INR";
    @Enumerated(EnumType.STRING) private Status status = Status.CREATED;
    @Column(name="paid_at") private LocalDateTime paidAt;
    @Column(name="refund_id") private String refundId;
    @Column(name="refunded_at") private LocalDateTime refundedAt;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status { CREATED, PAID, FAILED, REFUNDED }
}
