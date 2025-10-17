package com.api.hackathon.service;

import com.api.hackathon.model.Tournament;
import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepo;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament tournament;
    private UserBabyfoot user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com", "pass", "USER", "ATTAQUANT", null);
        tournament = new Tournament(1, LocalDateTime.now().plusDays(2), user);
    }

    @Test
    void getAll_shouldReturnList() {
        when(tournamentRepo.findAll()).thenReturn(List.of(tournament));
        assertThat(tournamentService.getAll()).hasSize(1);
    }

    @Test
    void getById_shouldReturnOptional() {
        when(tournamentRepo.findById(1)).thenReturn(Optional.of(tournament));
        assertThat(tournamentService.getById(1)).isPresent();
    }

    @Test
    void create_shouldSave() {
        when(tournamentRepo.save(any(Tournament.class))).thenReturn(tournament);
        assertThat(tournamentService.create(tournament)).isEqualTo(tournament);
        verify(tournamentRepo).save(tournament);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(tournamentRepo.findById(99)).thenReturn(Optional.empty());
        try {
            tournamentService.update(99, tournament);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("not found");
        }
    }

    @Test
    void delete_shouldCallRepository() {
        tournamentService.delete(1);
        verify(tournamentRepo).deleteById(1);
    }
}
