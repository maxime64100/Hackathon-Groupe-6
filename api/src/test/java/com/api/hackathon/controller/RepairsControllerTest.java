package com.api.hackathon.controller;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.model.Repairs;
import com.api.hackathon.service.RepairsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RepairsControllerTest {

    @Mock
    private RepairsService repairsService;

    @InjectMocks
    private RepairsController repairsController;

    private Repairs testRepair;
    private Babyfoot testBabyfoot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testBabyfoot = new Babyfoot(1, "Salle A", "free", true);
        testRepair = new Repairs(
                1,
                "Réparation manette gauche",
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                testBabyfoot
        );
    }

    @Test
    void getAll_shouldReturnList() {
        when(repairsService.getAll()).thenReturn(List.of(testRepair));

        ResponseEntity<List<Repairs>> response = repairsController.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(repairsService).getAll();
    }

    @Test
    void getById_shouldReturnRepairWhenExists() {
        when(repairsService.getById(1)).thenReturn(Optional.of(testRepair));

        ResponseEntity<?> response = repairsController.getById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testRepair);
    }

    @Test
    void getById_shouldReturnNotFoundWhenMissing() {
        when(repairsService.getById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = repairsController.getById(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Repairs not found with id 99");
    }

    @Test
    void create_shouldReturnCreatedWhenValid() {
        when(repairsService.create(any(Repairs.class))).thenReturn(testRepair);

        ResponseEntity<?> response = repairsController.create(testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testRepair);
        verify(repairsService).create(testRepair);
    }

    @Test
    void create_shouldReturnBadRequestWhenBabyfootMissing() {
        testRepair.setBabyfoot(null);

        ResponseEntity<?> response = repairsController.create(testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("babyfoot is required");
        verify(repairsService, never()).create(any());
    }

    @Test
    void create_shouldReturnBadRequestWhenDatesMissing() {
        testRepair.setStartDateRepairs(null);
        testRepair.setEndDateRepairs(null);

        ResponseEntity<?> response = repairsController.create(testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("startDateRepairs and endDateRepairs are required");
        verify(repairsService, never()).create(any());
    }

    @Test
    void create_shouldReturnBadRequestWhenEndBeforeStart() {
        testRepair.setStartDateRepairs(LocalDateTime.now());
        testRepair.setEndDateRepairs(LocalDateTime.now().minusDays(1));

        ResponseEntity<?> response = repairsController.create(testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("endDateRepairs must be after startDateRepairs");
        verify(repairsService, never()).create(any());
    }

    @Test
    void create_shouldReturnInternalServerErrorOnException() {
        when(repairsService.create(any())).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<?> response = repairsController.create(testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Error creating repairs");
    }

    @Test
    void update_shouldReturnOkWhenValid() {
        when(repairsService.update(eq(1), any())).thenReturn(testRepair);

        ResponseEntity<?> response = repairsController.update(1, testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testRepair);
        verify(repairsService).update(eq(1), any());
    }

    @Test
    void update_shouldReturnBadRequestWhenBabyfootMissing() {
        testRepair.setBabyfoot(null);

        ResponseEntity<?> response = repairsController.update(1, testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("babyfoot is required");
        verify(repairsService, never()).update(anyInt(), any());
    }

    @Test
    void update_shouldReturnBadRequestWhenEndBeforeStart() {
        testRepair.setEndDateRepairs(testRepair.getStartDateRepairs().minusDays(5));

        ResponseEntity<?> response = repairsController.update(1, testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("endDateRepairs must be after startDateRepairs");
        verify(repairsService, never()).update(anyInt(), any());
    }

    @Test
    void update_shouldReturnNotFoundWhenMissing() {
        when(repairsService.update(eq(1), any())).thenThrow(new RuntimeException("Repairs not found"));

        ResponseEntity<?> response = repairsController.update(1, testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Repairs not found");
    }

    @Test
    void update_shouldReturnInternalServerErrorOnException() {
        when(repairsService.update(eq(1), any())).thenThrow(new IllegalStateException("Unexpected error"));

        ResponseEntity<?> response = repairsController.update(1, testRepair);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        // ✅ Nouveau message attendu
        assertThat(response.getBody().toString()).contains("Unexpected error");
    }

    @Test
    void delete_shouldReturnNoContentWhenSuccess() {
        doNothing().when(repairsService).delete(1);

        ResponseEntity<?> response = repairsController.delete(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(repairsService).delete(1);
    }

    @Test
    void delete_shouldReturnInternalServerErrorOnException() {
        doThrow(new RuntimeException("Delete failed")).when(repairsService).delete(1);

        ResponseEntity<?> response = repairsController.delete(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Error deleting repairs");
    }
}
