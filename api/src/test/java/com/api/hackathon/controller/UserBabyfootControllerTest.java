package com.api.hackathon.controller;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserBabyfootControllerTest {

    @Mock
    private UserBabyfootRepository userRepository;

    @InjectMocks
    private UserBabyfootController userController;

    private UserBabyfoot testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com",
                "encodedPassword", "USER", "ATTAQUANT", null);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserBabyfoot> users = userController.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getMail()).isEqualTo("alice@example.com");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<UserBabyfoot> response = userController.getUserById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
    }

    @Test
    void getUserById_shouldReturnNotFoundWhenMissing() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<UserBabyfoot> response = userController.getUserById(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createUser_shouldSaveAndReturnUserWhenNewEmail() {
        when(userRepository.existsByMail(testUser.getMail())).thenReturn(false);
        when(userRepository.save(any(UserBabyfoot.class))).thenReturn(testUser);

        ResponseEntity<UserBabyfoot> response = userController.createUser(testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMail()).isEqualTo("alice@example.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_shouldReturnBadRequestWhenEmailAlreadyExists() {
        when(userRepository.existsByMail(testUser.getMail())).thenReturn(true);

        ResponseEntity<UserBabyfoot> response = userController.createUser(testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateFieldsWhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserBabyfoot.class))).thenReturn(testUser);

        UserBabyfoot updated = new UserBabyfoot();
        updated.setName("Alicia");
        updated.setProfileImageUrl("http://new.image.png");

        ResponseEntity<UserBabyfoot> response = userController.updateUser(1, updated);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Alicia");
        assertThat(response.getBody().getProfileImageUrl()).isEqualTo("http://new.image.png");
    }

    @Test
    void updateUser_shouldReturnNotFoundIfUserMissing() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<UserBabyfoot> response = userController.updateUser(999, testUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteUser_shouldReturnNoContentWhenDeleted() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_shouldReturnNotFoundIfMissing() {
        when(userRepository.existsById(2)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
