package com.homemitra.service;

import com.homemitra.dto.*;
import com.homemitra.model.User;
import com.homemitra.repository.UserRepository;
import com.homemitra.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest.Register req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        if (userRepository.existsByPhone(req.getPhone()))
            throw new RuntimeException("Phone already registered");

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.valueOf(req.getRole().toUpperCase()))
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .token(token).refreshToken(refresh)
                .role(user.getRole().name()).userId(user.getId())
                .fullName(user.getFullName()).email(user.getEmail())
                .build();
    }

    public AuthResponse login(AuthRequest.Login req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .token(token).refreshToken(refresh)
                .role(user.getRole().name()).userId(user.getId())
                .fullName(user.getFullName()).email(user.getEmail())
                .build();
    }
}
