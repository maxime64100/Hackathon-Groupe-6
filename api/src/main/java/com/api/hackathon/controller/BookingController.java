package com.api.hackathon.controller;

import com.api.hackathon.model.Booking;
import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.UserBabyfootRepository;
import com.api.hackathon.repository.BabyfootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "BearerAuth")

@Tag(name = "BookingController", description = "Endpoints protégés de l'API")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserBabyfootRepository userRepository;

    @Autowired
    private BabyfootRepository babyfootRepository;

    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @Operation(summary = "Créer ressource")
    @ApiResponse(responseCode = "201", description = "Créé")

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        if (booking.getUser() == null ||
                !userRepository.existsById(booking.getUser().getIdUser())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Utilisateur introuvable (id=" + booking.getUser().getIdUser() + ")"));
        }

        if (booking.getBabyfoot() == null ||
                !babyfootRepository.existsById(booking.getBabyfoot().getIdBabyfoot())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Babyfoot introuvable (id=" + booking.getBabyfoot().getIdBabyfoot() + ")"));
        }

        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Mettre à jour /{id}")
    @ApiResponse(responseCode = "200", description = "OK")

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable int id, @RequestBody Booking booking) {
        Optional<Booking> existing = bookingRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Réservation introuvable (id=" + id + ")"));
        }

        // Vérif user
        if (booking.getUser() == null ||
                !userRepository.existsById(booking.getUser().getIdUser())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Utilisateur introuvable (id=" + booking.getUser().getIdUser() + ")"));
        }

        // Vérif babyfoot
        if (booking.getBabyfoot() == null ||
                !babyfootRepository.existsById(booking.getBabyfoot().getIdBabyfoot())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Babyfoot introuvable (id=" + booking.getBabyfoot().getIdBabyfoot() + ")"));
        }

        Booking toUpdate = existing.get();
        toUpdate.setStatutBooking(booking.getStatutBooking());
        toUpdate.setDateBooking(booking.getDateBooking());
        toUpdate.setUser(booking.getUser());
        toUpdate.setBabyfoot(booking.getBabyfoot());

        Booking updated = bookingRepository.save(toUpdate);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer /{id}")
    @ApiResponse(responseCode = "204", description = "Supprimé")

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable int id) {
        Optional<Booking> existing = bookingRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Réservation introuvable (id=" + id + ")"));
        }

        bookingRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Réservation supprimée avec succès (id=" + id + ")"));
    }
}