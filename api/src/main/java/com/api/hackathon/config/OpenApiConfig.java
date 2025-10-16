package com.api.hackathon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
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
            
            Projet réalisé lors du Hackathon Ynov Toulouse 2025.
            """
        ),
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

