package com.homemitra.service;

import com.homemitra.dto.*;
import com.homemitra.model.*;
import com.homemitra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public BookingResponse createBooking(BookingRequest req, Long customerId) {
        com.homemitra.model.Service service = serviceRepository.findById(req.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));
        Address address = addressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal tax = service.getBasePrice().multiply(BigDecimal.valueOf(0.18));
        BigDecimal finalAmount = service.getBasePrice().add(tax);

        Booking booking = Booking.builder()
                .bookingRef("HM-" + UUID.randomUUID().toString().substring(0,8).toUpperCase())
                .customer(customer).service(service).address(address)
                .scheduledAt(req.getScheduledAt())
                .durationMins(service.getDurationMins())
                .amount(service.getBasePrice())
                .tax(tax).discount(BigDecimal.ZERO).finalAmount(finalAmount)
                .notes(req.getNotes()).status(Booking.Status.PENDING)
                .build();
        bookingRepository.save(booking);

        notificationService.create(customer, "Booking Confirmed",
                "Your booking " + booking.getBookingRef() + " has been placed!", Notification.Type.BOOKING);

        return toResponse(booking);
    }

    public Page<BookingResponse> getMyBookings(Long customerId, int page, int size) {
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, PageRequest.of(page, size))
                .map(this::toResponse);
    }

    @Transactional
    public BookingResponse updateStatus(Long bookingId, String status, Long actorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.Status.valueOf(status));
        bookingRepository.save(booking);

        // Push real-time update via WebSocket
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId,
                java.util.Map.of("bookingId", bookingId, "status", status,
                        "message", "Booking status updated to " + status,
                        "timestamp", LocalDateTime.now().toString()));

        notificationService.create(booking.getCustomer(), "Booking Update",
                "Your booking " + booking.getBookingRef() + " is now " + status, Notification.Type.BOOKING);

        return toResponse(booking);
    }

    public BookingResponse getByRef(String ref) {
        return bookingRepository.findByBookingRef(ref)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    private BookingResponse toResponse(Booking b) {
        Address a = b.getAddress();
        String addressLine = a.getLine1() + ", " + a.getCity() + " - " + a.getPincode();
        return BookingResponse.builder()
                .id(b.getId()).bookingRef(b.getBookingRef())
                .serviceName(b.getService().getName())
                .serviceIcon(b.getService().getCategory().getIcon())
                .providerName(b.getProvider() != null ? b.getProvider().getUser().getFullName() : "Assigning...")
                .addressLine(addressLine)
                .scheduledAt(b.getScheduledAt())
                .finalAmount(b.getFinalAmount())
                .status(b.getStatus().name())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
