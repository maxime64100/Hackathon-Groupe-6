package com.api.hackathon.controller;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.service.BabyfootService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/babyfoots")
@RequiredArgsConstructor
public class BabyfootController {

    private final BabyfootService babyfootService;

    @GetMapping
    public ResponseEntity<List<Babyfoot>> getAllBabyfoots() {
        return ResponseEntity.ok(babyfootService.getAllBabyfoots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBabyfootById(@PathVariable Integer id) {
        return babyfootService.getBabyfootById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Babyfoot not found with id " + id));
    }

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
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBabyfoot(@PathVariable Integer id, @RequestBody Babyfoot updatedBabyfoot) {
        try {
            Babyfoot updated = babyfootService.updateBabyfoot(id, updatedBabyfoot);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating babyfoot: " + e.getMessage());
        }
    }

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