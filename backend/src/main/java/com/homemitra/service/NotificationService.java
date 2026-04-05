package com.homemitra.service;

import com.homemitra.model.*;
import com.homemitra.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification create(User user, String title, String body, Notification.Type type) {
        Notification n = Notification.builder()
                .user(user).title(title).body(body).type(type).build();
        return notificationRepository.save(n);
    }

    public List<Notification> getForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public void markAllRead(Long userId) {
        notificationRepository.markAllReadForUser(userId);
    }
}
