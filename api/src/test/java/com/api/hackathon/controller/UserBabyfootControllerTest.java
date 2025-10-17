package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.TournamentRepository;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserBabyfootControllerTest {

    @Mock
    private UserBabyfootRepository userRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private UserBabyfootController userController;

    private UserBabyfoot testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com",
                "password", "USER", "ATTAQUANT", "https://image.png");
    }

    // 🔹 GET ALL USERS (paged)
    @Test
    void getAllUsers_shouldReturnPagedUsers() {
        Page<UserBabyfoot> page = new PageImpl<>(List.of(testUser), PageRequest.of(0, 5), 1);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Map<String, Object>> response = userController.getAllUsers(0, 5);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(((List<UserBabyfoot>) body.get("users"))).hasSize(1);
        assertThat(body.get("currentPage")).isEqualTo(0);
        assertThat(body.get("totalItems")).isEqualTo(1L);
        assertThat(body.get("totalPages")).isEqualTo(1);

        verify(userRepository).findAll(any(PageRequest.class));
    }

    // 🔹 GET USER BY ID (found)
    @Test
    void getUserById_shouldReturnUserIfExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<UserBabyfoot> response = userController.getUserById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
        verify(userRepository).findById(1);
    }

    // 🔹 GET USER BY ID (not found)
    @Test
    void getUserById_shouldReturnNotFoundIfMissing() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<UserBabyfoot> response = userController.getUserById(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userRepository).findById(2);
    }

    // 🔹 CREATE USER (email unique)
    @Test
    void createUser_shouldSaveAndReturnUser() {
        when(userRepository.existsByMail(testUser.getMail())).thenReturn(false);
        when(userRepository.save(any(UserBabyfoot.class))).thenReturn(testUser);

        ResponseEntity<UserBabyfoot> response = userController.createUser(testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
        verify(userRepository).save(testUser);
    }

    // 🔹 CREATE USER (email already exists)
    @Test
    void createUser_shouldReturnBadRequestIfEmailExists() {
        when(userRepository.existsByMail(testUser.getMail())).thenReturn(true);

        ResponseEntity<UserBabyfoot> response = userController.createUser(testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userRepository, never()).save(any());
    }

    // 🔹 UPDATE USER (found)
    @Test
    void updateUser_shouldUpdateFieldsIfUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserBabyfoot.class))).thenReturn(testUser);

        UserBabyfoot updated = new UserBabyfoot();
        updated.setName("Alicia");
        updated.setSurname("Durand");
        updated.setProfileImageUrl("https://newimage.png");

        ResponseEntity<UserBabyfoot> response = userController.updateUser(1, updated);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Alicia");
        assertThat(response.getBody().getSurname()).isEqualTo("Durand");
        assertThat(response.getBody().getProfileImageUrl()).isEqualTo("https://newimage.png");

        verify(userRepository).findById(1);
        verify(userRepository).save(any(UserBabyfoot.class));
    }

    // 🔹 UPDATE USER (not found)
    @Test
    void updateUser_shouldReturnNotFoundIfUserMissing() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<UserBabyfoot> response = userController.updateUser(99, testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userRepository).findById(99);
    }

    // 🔹 DELETE USER (found)
    @Test
    void deleteUser_shouldDeleteAllAssociatedData() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookingRepository).deleteAllByUser(testUser);
        verify(tournamentRepository).deleteAllByUser(testUser);
        verify(userRepository).delete(testUser);
    }

    // 🔹 DELETE USER (not found)
    @Test
    void deleteUser_shouldReturnNotFoundIfUserMissing() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookingRepository, never()).deleteAllByUser(any());
        verify(tournamentRepository, never()).deleteAllByUser(any());
        verify(userRepository, never()).delete(any());
    }
}
