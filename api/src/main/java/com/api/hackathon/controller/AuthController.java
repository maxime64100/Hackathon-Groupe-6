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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController

@Tag(name = "AuthController", description = "Endpoints publics d'authentification")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserBabyfootRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;    @Operation(summary = "Créer /register")
    @ApiResponse(responseCode = "201", description = "Créé")


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserBabyfoot user) {
        user.setPasswordUser(passwordEncoder.encode(user.getPasswordUser()));
        userRepo.save(user);
        return ResponseEntity.ok("Utilisateur enregistré !");
    }    @Operation(summary = "Créer /login")
    @ApiResponse(responseCode = "201", description = "Créé")


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword())
        );

        UserBabyfoot user = userRepo.findByMail(request.getMail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getMail(),
                        user.getPasswordUser(),
                        List.of(() -> "USER")
                )
        );

        // ✅ On renvoie maintenant aussi l'id de l'utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getIdUser());

        return ResponseEntity.ok(response);
    }

}

@Data
class AuthRequest {
    private String mail;
    private String password;
}