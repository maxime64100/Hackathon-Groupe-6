package com.api.hackathon.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Schema(name = "User", description = "Utilisateur de l’application Babyfoot")
@Entity
@Table(name = "user_babyfoot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBabyfoot {
    @Schema(description = "Identifiant utilisateur", example = "1")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Schema(description = "Prénom", example = "Raphaël")
    private String name;

    @Schema(description = "Nom", example = "Queron")
    private String surname;

    @Schema(description = "Adresse email unique", example = "raphael.queron@ynov.com")
    private String mail;

    @Schema(description = "Mot de passe (hashé côté API)", example = "$2a$10$...")
    private String passwordUser;

    @Schema(description = "Rôle applicatif", example = "USER")
    private String role = "USER";

    @Schema(description = "Rôle en jeu (optionnel)", example = "attaquant")
    private String roleGame;

    @Schema(description = "Photo de profil de l'utilisateur", example = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FDeftones_%2528album%2529&psig=AOvVaw062ltu-wuYjcYT67ir-llJ&ust=1760740443784000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCMi8orfjqZADFQAAAAAdAAAAABAE")
    private String profileImageUrl;
}