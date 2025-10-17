package com.api.hackathon.repository;

import com.api.hackathon.model.Booking;
import com.api.hackathon.model.UserBabyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser(UserBabyfoot user);
    void deleteAllByUser(UserBabyfoot user);
}