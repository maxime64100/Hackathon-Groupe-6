package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 🔓 autorise les requêtes depuis ton front Angular
public class UserBabyfootController {

    private final UserBabyfootRepository userRepository;

    public UserBabyfootController(UserBabyfootRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 GET - Récupérer tous les utilisateurs
    @GetMapping
    public List<UserBabyfoot> getAllUsers() {
        return userRepository.findAll();
    }

    // 🔹 GET - Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserBabyfoot> getUserById(@PathVariable Integer id) {
        Optional<UserBabyfoot> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 POST - Créer un utilisateur
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
    @PutMapping("/{id}")
    public ResponseEntity<UserBabyfoot> updateUser(@PathVariable Integer id, @RequestBody UserBabyfoot updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getName() != null) {
                        existingUser.setName(updatedUser.getName());
                    }
                    if (updatedUser.getSurname() != null) {
                        existingUser.setSurname(updatedUser.getSurname());
                    }
                    if (updatedUser.getMail() != null) {
                        existingUser.setMail(updatedUser.getMail());
                    }
                    if (updatedUser.getPasswordUser() != null) {
                        existingUser.setPasswordUser(updatedUser.getPasswordUser());
                    }

                    userRepository.save(existingUser);
                    return ResponseEntity.ok(existingUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // 🔹 DELETE - Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
