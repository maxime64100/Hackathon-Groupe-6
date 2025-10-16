package com.api.hackathon.service;

import com.api.hackathon.model.Booking;
import com.api.hackathon.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Integer id, Booking updatedBooking) {
        return bookingRepository.findById(id)
                .map(existing -> {
                    existing.setStatutBooking(updatedBooking.getStatutBooking());
                    existing.setDateBooking(updatedBooking.getDateBooking());
                    existing.setUser(updatedBooking.getUser());
                    existing.setBabyfoot(updatedBooking.getBabyfoot());
                    return bookingRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }
}