package com.homemitra.service;

import com.homemitra.dto.PaymentRequest;
import com.homemitra.model.Booking;
import com.homemitra.model.Payment; // Your local entity
import com.homemitra.repository.BookingRepository;
import com.homemitra.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Value("${razorpay.key.id}")
    private String keyId;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Map<String, Object> createOrder(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject options = new JSONObject();
        // Convert BigDecimal to Paise (multiply by 100)
        options.put("amount", booking.getFinalAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue());
        options.put("currency", "INR");
        options.put("receipt", booking.getBookingRef());

        Order order = client.orders.create(options);

        // Explicitly use your model Payment to avoid ambiguity with com.razorpay.Payment
        Payment payment = Payment.builder()
                .booking(booking)
                .razorpayOrderId(order.get("id"))
                .amount(booking.getFinalAmount())
                .status(Payment.Status.CREATED)
                .build();
        paymentRepository.save(payment);

        return Map.of(
                "orderId", order.get("id").toString(),
                "amount", booking.getFinalAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue(),
                "currency", "INR",
                "keyId", keyId
        );
    }

    @Transactional
    public boolean verifyAndCapture(PaymentRequest req) throws Exception {
        // 1. Fix the "cannot find symbol" by using the correct SDK verification method
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", req.getRazorpayOrderId());
        options.put("razorpay_payment_id", req.getRazorpayPaymentId());
        options.put("razorpay_signature", req.getRazorpaySignature());

        // verifyPaymentSignature returns true or throws a RazorpayException
        boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

        if (!isValid) {
            return false;
        }

        // 2. Fix "ambiguous reference" by ensuring we use our entity 'Payment'
        // If the error persists, change 'Payment' to 'com.homemitra.model.Payment' below
        Payment payment = paymentRepository.findByBookingId(req.getBookingId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        payment.setRazorpayPaymentId(req.getRazorpayPaymentId());
        payment.setRazorpaySignature(req.getRazorpaySignature());
        payment.setStatus(Payment.Status.PAID);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Booking booking = payment.getBooking();
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);
        
        return true;
    }
}