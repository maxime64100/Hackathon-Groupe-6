package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import com.api.hackathon.service.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserBabyfootRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserBabyfoot user) {
        user.setPasswordUser(passwordEncoder.encode(user.getPasswordUser()));
        userRepo.save(user);
        return ResponseEntity.ok("Utilisateur enregistré !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));

        UserBabyfoot user = userRepo.findByMail(request.getMail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getMail(), user.getPasswordUser(), List.of(() -> "USER"))
        );

        return ResponseEntity.ok(Map.of("token", token));
    }
}

@Data
class AuthRequest {
    private String mail;
    private String password;
}