package com.api.hackathon.controller;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.service.BabyfootService;
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

@Tag(name = "BabyfootController", description = "Endpoints protégés de l'API")
@ApiResponse(responseCode = "200", description = "OK")

@RequestMapping("/api/babyfoots")
@RequiredArgsConstructor
public class BabyfootController {

    private final BabyfootService babyfootService;    @Operation(summary = "Lister/Récupérer ressource")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping
    public ResponseEntity<List<Babyfoot>> getAllBabyfoots() {
        return ResponseEntity.ok(babyfootService.getAllBabyfoots());
    }    @Operation(summary = "Lister/Récupérer /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @GetMapping("/{id}")
    public ResponseEntity<?> getBabyfootById(@PathVariable Integer id) {
        return babyfootService.getBabyfootById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Babyfoot not found with id " + id));
    }    @Operation(summary = "Créer ressource")
    @ApiResponse(responseCode = "201", description = "Créé")


    @PostMapping
    public ResponseEntity<?> createBabyfoot(@RequestBody Babyfoot babyfoot) {
        try {
            if (babyfoot.getPlace() == null || babyfoot.getPlace().isEmpty()) {
                return ResponseEntity.badRequest().body("Place is required");
            }
            Babyfoot created = babyfootService.createBabyfoot(babyfoot);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating babyfoot: " + e.getMessage());
        }
    }    @Operation(summary = "Mettre à jour /{id}")
    @ApiResponse(responseCode = "200", description = "OK")


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBabyfoot(@PathVariable Integer id, @RequestBody Babyfoot updatedBabyfoot) {
        try {
            Babyfoot updated = babyfootService.updateBabyfoot(id, updatedBabyfoot);
            return ResponseEntity.ok(updated);
        }
        catch (RuntimeException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating babyfoot: " + e.getMessage());
        }
    }    @Operation(summary = "Supprimer /{id}")
    @ApiResponse(responseCode = "204", description = "Supprimé")


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBabyfoot(@PathVariable Integer id) {
        try {
            babyfootService.deleteBabyfoot(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting babyfoot: " + e.getMessage());
        }
    }
}