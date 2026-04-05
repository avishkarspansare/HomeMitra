package com.homemitra.controller;

import com.homemitra.dto.*;
import com.homemitra.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.homemitra.repository.UserRepository;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(
            @Valid @RequestBody BookingRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(ApiResponse.ok("Booking created", bookingService.createBooking(req, userId)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> myBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size) {
        Long userId = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getMyBookings(userId, page, size)));
    }

    @GetMapping("/{ref}")
    public ResponseEntity<ApiResponse<BookingResponse>> getByRef(@PathVariable String ref) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getByRef(ref)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(ApiResponse.ok(bookingService.updateStatus(id, status, userId)));
    }
}
