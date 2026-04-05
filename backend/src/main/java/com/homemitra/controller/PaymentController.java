package com.homemitra.controller;

import com.homemitra.dto.*;
import com.homemitra.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createOrder(@PathVariable Long bookingId) throws Exception {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.createOrder(bookingId)));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verify(@RequestBody PaymentRequest req) throws Exception {
        boolean ok = paymentService.verifyAndCapture(req);
        return ResponseEntity.ok(ok ? ApiResponse.ok("Payment verified", true)
                : ApiResponse.error("Payment verification failed"));
    }
}
