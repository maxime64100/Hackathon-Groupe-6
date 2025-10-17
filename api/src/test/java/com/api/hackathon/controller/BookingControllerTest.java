package com.api.hackathon.controller;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.model.Booking;
import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.BabyfootRepository;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserBabyfootRepository userRepository;
    @Mock private BabyfootRepository babyfootRepository;

    @InjectMocks private BookingController bookingController;

    private Booking testBooking;
    private UserBabyfoot testUser;
    private Babyfoot testBabyfoot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com", "pass", "USER", "ATTAQUANT", null);
        testBabyfoot = new Babyfoot(1, "Salle A", "free", true);
        testBooking = new Booking(1, "confirmed", LocalDateTime.now(), testUser, testBabyfoot);
    }

    @Test
    void getAllBookings_shouldReturnList() {
        when(bookingRepository.findAll()).thenReturn(List.of(testBooking));

        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void createBooking_shouldReturnCreatedWhenValid() {
        when(userRepository.existsById(testUser.getIdUser())).thenReturn(true);
        when(babyfootRepository.existsById(testBabyfoot.getIdBabyfoot())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        ResponseEntity<?> response = bookingController.createBooking(testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testBooking);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void createBooking_shouldReturnBadRequestWhenUserNotFound() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<?> response = bookingController.createBooking(testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).contains("Utilisateur introuvable");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_shouldReturnBadRequestWhenBabyfootNotFound() {
        when(userRepository.existsById(testUser.getIdUser())).thenReturn(true);
        when(babyfootRepository.existsById(testBabyfoot.getIdBabyfoot())).thenReturn(false);

        ResponseEntity<?> response = bookingController.createBooking(testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).contains("Babyfoot introuvable");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_shouldReturnUpdatedWhenValid() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(testUser.getIdUser())).thenReturn(true);
        when(babyfootRepository.existsById(testBabyfoot.getIdBabyfoot())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        ResponseEntity<?> response = bookingController.updateBooking(1, testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testBooking);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_shouldReturnNotFoundWhenMissing() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookingController.updateBooking(1, testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().toString()).contains("Réservation introuvable");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_shouldReturnBadRequestWhenUserMissing() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<?> response = bookingController.updateBooking(1, testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).contains("Utilisateur introuvable");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_shouldReturnBadRequestWhenBabyfootMissing() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(babyfootRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<?> response = bookingController.updateBooking(1, testBooking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).contains("Babyfoot introuvable");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void deleteBooking_shouldReturnOkWhenSuccess() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(testBooking));

        ResponseEntity<?> response = bookingController.deleteBooking(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString()).contains("Réservation supprimée");
        verify(bookingRepository).deleteById(1);
    }

    @Test
    void deleteBooking_shouldReturnNotFoundWhenMissing() {
        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookingController.deleteBooking(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().toString()).contains("Réservation introuvable");
        verify(bookingRepository, never()).deleteById(any());
    }
}
