package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import com.api.hackathon.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private UserBabyfootRepository userRepo;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private UserBabyfoot testUser;
    private AuthRequest testRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com",
                "encodedPass", "USER", "ATTAQUANT", null);
        testRequest = new AuthRequest();
        testRequest.setMail("alice@example.com");
        testRequest.setPassword("password123");
    }

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(UserBabyfoot.class))).thenReturn(testUser);

        ResponseEntity<?> response = authController.register(testUser);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Utilisateur enregistré !");
        verify(passwordEncoder).encode(anyString());
        verify(userRepo).save(testUser);
    }

    @Test
    void login_shouldAuthenticateAndReturnToken() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepo.findByMail(testRequest.getMail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any())).thenReturn("mockedToken");

        ResponseEntity<?> response = authController.login(testRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);

        Map<String, Object> result = (HashMap<String, Object>) response.getBody();
        assertThat(result.get("token")).isEqualTo("mockedToken");
        assertThat(result.get("userId")).isEqualTo(testUser.getIdUser());
    }

    @Test
    void login_shouldThrowWhenUserNotFound() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepo.findByMail(anyString())).thenReturn(Optional.empty());

        try {
            authController.login(testRequest);
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage()).isEqualTo("User not found");
        }

        verify(userRepo).findByMail(testRequest.getMail());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldThrowWhenAuthenticationFails() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        try {
            authController.login(testRequest);
        } catch (Exception ex) {
            assertThat(ex.getMessage()).contains("Bad credentials");
        }

        verify(userRepo, never()).findByMail(any());
        verify(jwtService, never()).generateToken(any());
    }
}
