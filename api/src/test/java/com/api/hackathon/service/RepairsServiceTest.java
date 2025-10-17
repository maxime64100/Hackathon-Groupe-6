package com.api.hackathon.service;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.model.Repairs;
import com.api.hackathon.repository.RepairsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RepairsServiceTest {

    @InjectMocks
    private RepairsService repairsService;

    @Mock
    private RepairsRepository repairsRepository;

    private Repairs existingRepair;
    private Repairs payloadRepair;
    private Babyfoot oldBabyfoot;
    private Babyfoot newBabyfoot;
    private Repairs repair;
    private Babyfoot baby;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        baby = new Babyfoot(1, "Salle A", "free", true);
        repair = new Repairs(1, "free", LocalDateTime.now().minusDays(2), LocalDateTime.now(), baby);

        oldBabyfoot = new Babyfoot(1, "Salle A", "free", true);
        newBabyfoot = new Babyfoot(2, "Salle B", "occupied", true);

        existingRepair = new Repairs(1, "free", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), oldBabyfoot);

        payloadRepair = new Repairs(null,
                "Changement barres",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                newBabyfoot
        );
    }

    @Test
    void getAll_shouldReturnList() {
        when(repairsRepository.findAll()).thenReturn(List.of(repair));

        List<Repairs> result = repairsService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatusBabyfoot()).isEqualTo("free");
        verify(repairsRepository).findAll();
    }

    @Test
    void getById_shouldReturnOptional() {
        when(repairsRepository.findById(1)).thenReturn(Optional.of(repair));

        Optional<Repairs> result = repairsService.getById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getStatusBabyfoot()).isEqualTo("free");
        verify(repairsRepository).findById(1);
    }


    @Test
    void create_shouldSave() {
        when(repairsRepository.save(any(Repairs.class))).thenReturn(repair);

        Repairs result = repairsService.create(repair);

        assertThat(result).isEqualTo(repair);
        verify(repairsRepository).save(repair);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(repairsRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repairsService.update(99, repair))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");

        verify(repairsRepository).findById(99);
        verify(repairsRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateAllFieldsAndSave() {
        when(repairsRepository.findById(1)).thenReturn(Optional.of(existingRepair));
        when(repairsRepository.save(any(Repairs.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Repairs updated = repairsService.update(1, payloadRepair);

        assertThat(updated.getStartDateRepairs()).isEqualTo(payloadRepair.getStartDateRepairs());
        assertThat(updated.getEndDateRepairs()).isEqualTo(payloadRepair.getEndDateRepairs());
        assertThat(updated.getBabyfoot()).isEqualTo(payloadRepair.getBabyfoot());
        assertThat(updated.getStatusBabyfoot()).isEqualTo(payloadRepair.getStatusBabyfoot());

        verify(repairsRepository).findById(1);
        verify(repairsRepository).save(existingRepair);
    }

    @Test
    void delete_shouldCallRepository() {
        repairsService.delete(1);
        verify(repairsRepository).deleteById(1);
    }
}
