package com.homemitra.controller;

import com.homemitra.dto.ApiResponse;
import com.homemitra.model.Notification;
import com.homemitra.repository.UserRepository;
import com.homemitra.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> get(@AuthenticationPrincipal UserDetails ud) {
        Long userId = userRepository.findByEmail(ud.getUsername()).orElseThrow().getId();
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getForUser(userId)));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> unread(@AuthenticationPrincipal UserDetails ud) {
        Long userId = userRepository.findByEmail(ud.getUsername()).orElseThrow().getId();
        return ResponseEntity.ok(ApiResponse.ok(notificationService.countUnread(userId)));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<ApiResponse<Void>> markRead(@AuthenticationPrincipal UserDetails ud) {
        Long userId = userRepository.findByEmail(ud.getUsername()).orElseThrow().getId();
        notificationService.markAllRead(userId);
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", null));
    }
}
