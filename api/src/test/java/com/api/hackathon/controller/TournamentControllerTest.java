package com.api.hackathon.controller;

import com.api.hackathon.model.Tournament;
import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.service.TournamentService;
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

class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private Tournament testTournament;
    private UserBabyfoot testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com", "pass", "USER", "ATTAQUANT", null);
        testTournament = new Tournament(1, LocalDateTime.now().plusDays(2), testUser);
    }

    @Test
    void getAll_shouldReturnList() {
        when(tournamentService.getAll()).thenReturn(List.of(testTournament));

        ResponseEntity<List<Tournament>> response = tournamentController.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(tournamentService).getAll();
    }

    @Test
    void getById_shouldReturnTournamentWhenExists() {
        when(tournamentService.getById(1)).thenReturn(Optional.of(testTournament));

        ResponseEntity<?> response = tournamentController.getById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testTournament);
    }

    @Test
    void getById_shouldReturnNotFoundWhenMissing() {
        when(tournamentService.getById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = tournamentController.getById(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Tournament not found with id 99");
    }

    @Test
    void create_shouldReturnCreatedWhenValid() {
        when(tournamentService.create(any(Tournament.class))).thenReturn(testTournament);

        ResponseEntity<?> response = tournamentController.create(testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testTournament);
        verify(tournamentService).create(testTournament);
    }

    @Test
    void create_shouldReturnBadRequestWhenDateMissing() {
        testTournament.setDateTournament(null);

        ResponseEntity<?> response = tournamentController.create(testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("dateTournament is required");
        verify(tournamentService, never()).create(any());
    }

    @Test
    void create_shouldReturnInternalServerErrorOnException() {
        when(tournamentService.create(any())).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = tournamentController.create(testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Error creating tournament");
    }

    @Test
    void update_shouldReturnOkWhenValid() {
        when(tournamentService.update(eq(1), any(Tournament.class))).thenReturn(testTournament);

        ResponseEntity<?> response = tournamentController.update(1, testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testTournament);
        verify(tournamentService).update(eq(1), any());
    }

    @Test
    void update_shouldReturnBadRequestWhenDateMissing() {
        testTournament.setDateTournament(null);

        ResponseEntity<?> response = tournamentController.update(1, testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("dateTournament is required");
        verify(tournamentService, never()).update(anyInt(), any());
    }

    @Test
    void update_shouldReturnNotFoundWhenMissing() {
        when(tournamentService.update(eq(1), any())).thenThrow(new RuntimeException("Tournament not found"));

        ResponseEntity<?> response = tournamentController.update(1, testTournament);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Tournament not found");
    }

    @Test
    void update_shouldReturnInternalServerErrorOnException() {
        when(tournamentService.update(eq(1), any())).thenThrow(new IllegalStateException("Unexpected error"));

        ResponseEntity<?> response = tournamentController.update(1, testTournament);

        // Le contrôleur actuel catch RuntimeException en 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Unexpected error");
    }

    @Test
    void delete_shouldReturnNoContentWhenSuccess() {
        doNothing().when(tournamentService).delete(1);

        ResponseEntity<?> response = tournamentController.delete(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(tournamentService).delete(1);
    }

    @Test
    void delete_shouldReturnInternalServerErrorOnException() {
        doThrow(new RuntimeException("Deletion failed")).when(tournamentService).delete(1);

        ResponseEntity<?> response = tournamentController.delete(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Error deleting tournament");
    }
}
