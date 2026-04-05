package com.homemitra.dto;

import lombok.*;

@Data @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String role;
    private Long userId;
    private String fullName;
    private String email;
}
