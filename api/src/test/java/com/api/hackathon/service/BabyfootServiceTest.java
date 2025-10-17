package com.api.hackathon.service;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.repository.BabyfootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BabyfootServiceTest {

    @Mock
    private BabyfootRepository babyfootRepo;

    @InjectMocks
    private BabyfootService babyfootService;

    private Babyfoot baby;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baby = new Babyfoot(1, "Salle A", "free", true);
    }

    @Test
    void getAll_shouldReturnList() {
        when(babyfootRepo.findAll()).thenReturn(List.of(baby));
        List<Babyfoot> list = babyfootService.getAllBabyfoots();
        assertThat(list).hasSize(1);
    }

    @Test
    void getById_shouldReturnWhenExists() {
        when(babyfootRepo.findById(1)).thenReturn(Optional.of(baby));
        assertThat(babyfootService.getBabyfootById(1)).isPresent();
    }

    @Test
    void create_shouldSave() {
        when(babyfootRepo.save(any(Babyfoot.class))).thenReturn(baby);
        assertThat(babyfootService.createBabyfoot(baby)).isEqualTo(baby);
        verify(babyfootRepo).save(baby);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(babyfootRepo.findById(99)).thenReturn(Optional.empty());
        try {
            babyfootService.updateBabyfoot(99, baby);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("not found");
        }
    }

    @Test
    void delete_shouldCallRepository() {
        babyfootService.deleteBabyfoot(1);
        verify(babyfootRepo).deleteById(1);
    }
}
