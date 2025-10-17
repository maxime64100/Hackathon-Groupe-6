package com.api.hackathon.controller;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.service.BabyfootService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BabyfootControllerTest {

    @Mock
    private BabyfootService babyfootService;

    @InjectMocks
    private BabyfootController babyfootController;

    private Babyfoot testBabyfoot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testBabyfoot = new Babyfoot(1, "Salle A", "free", true);
    }

    @Test
    void getAllBabyfoots_shouldReturnList() {
        when(babyfootService.getAllBabyfoots()).thenReturn(List.of(testBabyfoot));

        ResponseEntity<List<Babyfoot>> response = babyfootController.getAllBabyfoots();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getPlace()).isEqualTo("Salle A");
        verify(babyfootService, times(1)).getAllBabyfoots();
    }

    @Test
    void getBabyfootById_shouldReturnBabyfootIfExists() {
        when(babyfootService.getBabyfootById(1)).thenReturn(Optional.of(testBabyfoot));

        ResponseEntity<?> response = babyfootController.getBabyfootById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(Babyfoot.class);
        assertThat(((Babyfoot) response.getBody()).getPlace()).isEqualTo("Salle A");
    }

    @Test
    void getBabyfootById_shouldReturnNotFoundIfMissing() {
        when(babyfootService.getBabyfootById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = babyfootController.getBabyfootById(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Babyfoot not found with id 99");
    }

    @Test
    void createBabyfoot_shouldReturnCreatedWhenValid() {
        when(babyfootService.createBabyfoot(any(Babyfoot.class))).thenReturn(testBabyfoot);

        ResponseEntity<?> response = babyfootController.createBabyfoot(testBabyfoot);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testBabyfoot);
        verify(babyfootService).createBabyfoot(testBabyfoot);
    }

    @Test
    void createBabyfoot_shouldReturnBadRequestWhenPlaceMissing() {
        Babyfoot invalid = new Babyfoot(2, "", "free", true);

        ResponseEntity<?> response = babyfootController.createBabyfoot(invalid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Place is required");
        verify(babyfootService, never()).createBabyfoot(any());
    }

    @Test
    void createBabyfoot_shouldReturnInternalServerErrorOnException() {
        when(babyfootService.createBabyfoot(any())).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = babyfootController.createBabyfoot(testBabyfoot);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).asString().contains("Error creating babyfoot");
    }

    @Test
    void updateBabyfoot_shouldReturnUpdatedWhenSuccess() {
        Babyfoot updated = new Babyfoot(1, "Salle B", "occupied", true);
        when(babyfootService.updateBabyfoot(1, updated)).thenReturn(updated);

        ResponseEntity<?> response = babyfootController.updateBabyfoot(1, updated);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Babyfoot) response.getBody()).getPlace()).isEqualTo("Salle B");
    }

    @Test
    void updateBabyfoot_shouldReturnNotFoundWhenMissing() {
        when(babyfootService.updateBabyfoot(eq(99), any()))
                .thenThrow(new RuntimeException("Babyfoot not found"));

        ResponseEntity<?> response = babyfootController.updateBabyfoot(99, testBabyfoot);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Babyfoot not found");
    }

    @Test
    void updateBabyfoot_shouldReturnInternalServerErrorOnUnexpectedError() {
        when(babyfootService.updateBabyfoot(eq(1), any()))
                .thenThrow(new IllegalStateException("Unexpected error"));

        ResponseEntity<?> response = babyfootController.updateBabyfoot(1, testBabyfoot);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Unexpected error");
    }


    @Test
    void deleteBabyfoot_shouldReturnNoContentWhenSuccess() {
        doNothing().when(babyfootService).deleteBabyfoot(1);

        ResponseEntity<?> response = babyfootController.deleteBabyfoot(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(babyfootService).deleteBabyfoot(1);
    }

    @Test
    void deleteBabyfoot_shouldReturnInternalServerErrorOnException() {
        doThrow(new RuntimeException("Deletion failed")).when(babyfootService).deleteBabyfoot(1);

        ResponseEntity<?> response = babyfootController.deleteBabyfoot(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).asString().contains("Error deleting babyfoot");
    }
}
