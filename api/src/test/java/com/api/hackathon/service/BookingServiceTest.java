package com.api.hackathon.service;

import com.api.hackathon.model.Booking;
import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.BookingRepository;
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

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private UserBabyfoot user;
    private Babyfoot baby;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com", "pass", "USER", "ATTAQUANT", null);
        baby = new Babyfoot(1, "Salle A", "free", true);
        booking = new Booking(1, "confirmed", LocalDateTime.now(), user, baby);
    }

    @Test
    void getAll_shouldReturnList() {
        when(bookingRepo.findAll()).thenReturn(List.of(booking));
        assertThat(bookingService.getAllBookings()).hasSize(1);
    }

    @Test
    void getById_shouldReturnOptional() {
        when(bookingRepo.findById(1)).thenReturn(Optional.of(booking));
        assertThat(bookingService.getBookingById(1)).isPresent();
    }

    @Test
    void create_shouldSaveBooking() {
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);
        assertThat(bookingService.createBooking(booking)).isEqualTo(booking);
        verify(bookingRepo).save(booking);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(bookingRepo.findById(1)).thenReturn(Optional.empty());
        try {
            bookingService.updateBooking(1, booking);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("not found");
        }
    }

    @Test
    void delete_shouldCallRepository() {
        bookingService.deleteBooking(1);
        verify(bookingRepo).deleteById(1);
    }
}
