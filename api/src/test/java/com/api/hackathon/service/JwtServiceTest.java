package com.api.hackathon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = User.withUsername("alice@example.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void generateToken_shouldReturnNonEmptyToken() {
        String token = jwtService.generateToken(userDetails);
        assertThat(token).isNotNull();
        assertThat(token).contains(".");
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertThat(username).isEqualTo(userDetails.getUsername());
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails otherUser = User.withUsername("bob@example.com")
                .password("pass")
                .roles("USER")
                .build();
        boolean valid = jwtService.isTokenValid(token, otherUser);
        assertThat(valid).isFalse();
    }
}
