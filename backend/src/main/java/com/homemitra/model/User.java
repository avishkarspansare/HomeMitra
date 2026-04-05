package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="full_name", nullable=false) private String fullName;
    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false, unique=true) private String phone;
    @Column(name="password_hash", nullable=false) private String passwordHash;
    @Enumerated(EnumType.STRING) private Role role = Role.CUSTOMER;
    @Column(name="avatar_url") private String avatarUrl;
    @Column(name="is_verified") private boolean verified = false;
    @Column(name="is_active") private boolean active = true;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name="updated_at") private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Role { CUSTOMER, PROVIDER, ADMIN }

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
