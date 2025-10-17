package com.api.hackathon.config;

import com.api.hackathon.model.*;
import com.api.hackathon.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initDatabase(
            UserBabyfootRepository userRepo,
            BabyfootRepository babyfootRepo,
            BookingRepository bookingRepo,
            TournamentRepository tournamentRepo,
            RepairsRepository repairsRepo
    ) {
        return args -> {
            System.out.println("Réinitialisation des données de test...");

            // On supprime les anciennes données sans dropper les tables
            bookingRepo.deleteAll();
            tournamentRepo.deleteAll();
            repairsRepo.deleteAll();
            babyfootRepo.deleteAll();
            userRepo.deleteAll();

            // USERS (hashés)
            UserBabyfoot alice = new UserBabyfoot(null, "Alice", "Dupont", "alice@example.com",
                    passwordEncoder.encode("azerty123"), "USER", "ATTAQUANT", null);
            UserBabyfoot bob = new UserBabyfoot(null, "Bob", "Martin", "bob@example.com",
                    passwordEncoder.encode("password123"), "USER", "DEFENSEUR", null);
            UserBabyfoot admin = new UserBabyfoot(null, "Admin", "Root", "admin@example.com",
                    passwordEncoder.encode("admin123"), "ADMIN", "COACH", null);

            userRepo.saveAll(List.of(alice, bob, admin));

            // BABYFOOTS
            Babyfoot baby1 = new Babyfoot(null, "Salle A", "free", true);
            Babyfoot baby2 = new Babyfoot(null, "Salle B", "occupied", true);
            Babyfoot baby3 = new Babyfoot(null, "Salle C", "maintenance", false);
            babyfootRepo.saveAll(List.of(baby1, baby2, baby3));

            // BOOKINGS
            Booking booking1 = new Booking(null, "confirmed", LocalDateTime.now().plusDays(1), alice, baby1);
            Booking booking2 = new Booking(null, "cancelled", LocalDateTime.now().plusDays(2), bob, baby2);
            Booking booking3 = new Booking(null, "pending", LocalDateTime.now().plusDays(3), alice, baby2);
            bookingRepo.saveAll(List.of(booking1, booking2, booking3));

            // TOURNAMENTS
            Tournament tour1 = new Tournament(null, LocalDateTime.now().plusWeeks(1), admin);
            Tournament tour2 = new Tournament(null, LocalDateTime.now().plusWeeks(2), alice);
            Tournament tour3 = new Tournament(null, LocalDateTime.now().plusWeeks(3), bob);
            tournamentRepo.saveAll(List.of(tour1, tour2, tour3));

            // REPAIRS
            Repairs rep1 = new Repairs(null, "Réparation manette gauche", LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1), baby3);
            Repairs rep2 = new Repairs(null, "Nettoyage complet", LocalDateTime.now().minusDays(5),
                    LocalDateTime.now().minusDays(3), baby2);
            repairsRepo.saveAll(List.of(rep1, rep2));

            System.out.println("Données de test insérées avec succès !");
        };
    }
}
