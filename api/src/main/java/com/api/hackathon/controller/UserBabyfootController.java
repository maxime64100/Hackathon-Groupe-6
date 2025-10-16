package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "BearerAuth")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 🔓
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs de l’application Babyfoot")
public class UserBabyfootController {

    private final UserBabyfootRepository userRepository;

    public UserBabyfootController(UserBabyfootRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 GET - Récupérer tous les utilisateurs
    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")

    @GetMapping
    public List<UserBabyfoot> getAllUsers() {
        return userRepository.findAll();
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
                    // On ignore toute tentative de changement du mail ou du mot de passe
                    // -> rien à faire ici
                    userRepository.save(existingUser);
                    return ResponseEntity.ok(existingUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // 🔹 DELETE - Supprimer un utilisateur
    @Operation(summary = "Supprimer /{id}")
    @ApiResponse(responseCode = "204", description = "Supprimé")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}