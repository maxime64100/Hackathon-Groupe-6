package com.api.hackathon.controller;

import com.api.hackathon.model.Repairs;
import com.api.hackathon.service.RepairsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

@Tag(name = "RepairsController", description = "Endpoints protégés de l'API")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairsController {

    private final RepairsService service;    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping
    public ResponseEntity<List<Repairs>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }    @Operation(summary = "Lister/Récupérer /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Repairs not found with id " + id));
    }    @Operation(summary = "Créer ressource")
    @ApiResponse(responseCode = "201", description = "Créé")


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Repairs r) {
        try {
            if (r.getBabyfoot() == null) {
                return ResponseEntity.badRequest().body("babyfoot is required");
            }
            if (r.getStartDateRepairs() == null || r.getEndDateRepairs() == null) {
                return ResponseEntity.badRequest().body("startDateRepairs and endDateRepairs are required");
            }
            if (r.getEndDateRepairs().isBefore(r.getStartDateRepairs())) {
                return ResponseEntity.badRequest().body("endDateRepairs must be after startDateRepairs");
            }
            Repairs created = service.create(r);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating repairs: " + e.getMessage());
        }
    }    @Operation(summary = "Mettre à jour /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Repairs r) {
        try {
            if (r.getBabyfoot() == null) {
                return ResponseEntity.badRequest().body("babyfoot is required");
            }
            if (r.getStartDateRepairs() == null || r.getEndDateRepairs() == null) {
                return ResponseEntity.badRequest().body("startDateRepairs and endDateRepairs are required");
            }
            if (r.getEndDateRepairs().isBefore(r.getStartDateRepairs())) {
                return ResponseEntity.badRequest().body("endDateRepairs must be after startDateRepairs");
            }
            Repairs updated = service.update(id, r);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating repairs: " + e.getMessage());
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
                    .body("Error deleting repairs: " + e.getMessage());
        }
    }
}