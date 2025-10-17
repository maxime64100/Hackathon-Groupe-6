package com.api.hackathon.controller;

import com.api.hackathon.model.Tournament;
import com.api.hackathon.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "BearerAuth")

@Tag(name = "TournamentController", description = "Endpoints protégés de l'API")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService service;    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping
    public ResponseEntity<List<Tournament>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }    @Operation(summary = "Lister/Récupérer /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tournament not found with id " + id));
    }    @Operation(summary = "Créer ressource")
    @ApiResponse(responseCode = "201", description = "Créé")


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tournament t) {
        try {
            if (t.getDateTournament() == null) {
                return ResponseEntity.badRequest().body("dateTournament is required");
            }
            Tournament created = service.create(t);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating tournament: " + e.getMessage());
        }
    }    @Operation(summary = "Mettre à jour /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Tournament t) {
        try {
            if (t.getDateTournament() == null) {
                return ResponseEntity.badRequest().body("dateTournament is required");
            }
            Tournament updated = service.update(id, t);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating tournament: " + e.getMessage());
        }
    }    @Operation(summary = "Supprimer /{id}")
    @ApiResponse(responseCode = "204", description = "Supprimé")


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting tournament: " + e.getMessage());
        }
    }
}