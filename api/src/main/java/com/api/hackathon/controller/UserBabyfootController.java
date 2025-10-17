package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.TournamentRepository;
import com.api.hackathon.repository.UserBabyfootRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "BearerAuth")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 🔓
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs de l’application Babyfoot")
public class UserBabyfootController {

    private final UserBabyfootRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final BookingRepository bookingRepository;

    public UserBabyfootController(
            UserBabyfootRepository userRepository,
            TournamentRepository tournamentRepository,
            BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.bookingRepository = bookingRepository;
    }

    // 🔹 GET - Récupérer tous les utilisateurs
    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<UserBabyfoot> pageUsers = userRepository.findAll(paging);

        Map<String, Object> response = new HashMap<>();
        response.put("users", pageUsers.getContent());
        response.put("currentPage", pageUsers.getNumber());
        response.put("totalItems", pageUsers.getTotalElements());
        response.put("totalPages", pageUsers.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // 🔹 GET - Récupérer un utilisateur par ID
    @Operation(summary = "Lister/Récupérer /{id}")
    @ApiResponse(responseCode = "200", description = "OK")

    @GetMapping("/{id}")
    public ResponseEntity<UserBabyfoot> getUserById(@PathVariable Integer id) {
        Optional<UserBabyfoot> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 POST - Créer un utilisateur
    @Operation(summary = "Créer ressource")
    @ApiResponse(responseCode = "201", description = "Créé")

    @PostMapping
    public ResponseEntity<UserBabyfoot> createUser(@RequestBody UserBabyfoot newUser) {
        // Vérifie si un utilisateur avec le même email existe déjà
        if (userRepository.existsByMail(newUser.getMail())) {
            return ResponseEntity.badRequest().build();
        }
        UserBabyfoot savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    // 🔹 PUT - Mettre à jour un utilisateur existant
    @Operation(summary = "Mettre à jour /{id}")
    @ApiResponse(responseCode = "200", description = "OK")

    @PutMapping("/{id}")
    public ResponseEntity<UserBabyfoot> updateUser(
            @PathVariable Integer id,
            @RequestBody UserBabyfoot updatedUser) {

        return userRepository.findById(id)
                .map(existingUser -> {
                    // 🔒 On interdit toute modification du mail et du mot de passe
                    // (donc on ne touche JAMAIS à ces champs)
                    if (updatedUser.getName() != null) {
                        existingUser.setName(updatedUser.getName());
                    }
                    if (updatedUser.getSurname() != null) {
                        existingUser.setSurname(updatedUser.getSurname());
                    }
                    if (updatedUser
                            .getProfileImageUrl() != null) {
                        existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());
                    }
                    // On ignore toute tentative de changement du mail ou du mot de passe
                    // -> rien à faire ici
                    userRepository.save(existingUser);
                    return ResponseEntity.ok(existingUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @Operation(summary = "Supprimer un utilisateur et ses données associées")
    @ApiResponse(responseCode = "204", description = "Utilisateur, tournois et réservations supprimés")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<UserBabyfoot> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserBabyfoot user = userOpt.get();

        // 🔹 Étape 1 : supprimer les réservations associées
        bookingRepository.deleteAllByUser(user);

        // 🔹 Étape 2 : supprimer les tournois associés
        tournamentRepository.deleteAllByUser(user);

        // 🔹 Étape 3 : supprimer l'utilisateur
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}