package com.homemitra.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class AuthRequest {
    @Data
    public static class Register {
        @NotBlank private String fullName;
        @Email @NotBlank private String email;
        @Pattern(regexp="^[6-9]\\d{9}$", message="Invalid phone") private String phone;
        @Size(min=8, message="Password must be at least 8 chars") private String password;
        private String role = "CUSTOMER";
    }

    @Data
    public static class Login {
        @Email @NotBlank private String email;
        @NotBlank private String password;
    }
}
