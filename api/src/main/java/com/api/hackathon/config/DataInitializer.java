package com.api.hackathon.config;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserBabyfootRepository userRepo) {
        return args -> {
            if (userRepo.count() == 0) {
                userRepo.save(new UserBabyfoot(null, "Alice", "Dupont", "alice@example.com", "azerty123", null));
                userRepo.save(new UserBabyfoot(null, "Bob", "Martin", "bob@example.com", "password123", null));
                userRepo.save(new UserBabyfoot(null, "Charlie", "Durand", "charlie@example.com", "test1234", null));
                System.out.println("✅ Données de test insérées dans user_babyfoot");
            } else {
                System.out.println("ℹ️ Données déjà présentes, aucune insertion");
            }
        };
    }
}
