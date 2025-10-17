package com.api.hackathon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Babyfoot - Hackathon Ynov Toulouse 2025",
                version = "1.0.0",
                description = """
            API REST permettant de gérer :
            - Les utilisateurs (inscription, rôles, authentification)
            - Les babyfoots disponibles
            - Les réservations
            - les tournois 
            
            Projet réalisé lors du Hackathon Ynov Toulouse 2025.
            """
        ),
        tags = {
                @Tag(name = "01 -AuthController", description = "Endpoints publics d'authentification"),
                @Tag(name = "02 - Utilisateurs", description = "Gestion des utilisateurs"),
                @Tag(name = "03 - BabyfootController", description = "Gestion des babyfoots"),
                @Tag(name = "04 - BookingController", description = "Gestion des réservations"),
                @Tag(name = "05 - RepairsController", description = "Gestion des réparations"),
                @Tag(name = "06 - TournamentController", description = "Gestion des tournois")
        },
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Serveur local"
                )
        }
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}

