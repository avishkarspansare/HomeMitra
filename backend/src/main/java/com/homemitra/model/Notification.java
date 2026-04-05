package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="notifications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false) private String title;
    @Column(nullable=false, columnDefinition="TEXT") private String body;
    @Enumerated(EnumType.STRING) private Type type = Type.SYSTEM;
    @Column(name="is_read") private boolean read = false;
    @Column(columnDefinition="JSON") private String meta;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();

    public enum Type { BOOKING, PAYMENT, PROMO, SYSTEM }
}
