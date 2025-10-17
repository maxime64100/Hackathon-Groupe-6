package com.api.hackathon.repository;

import com.api.hackathon.model.Booking;
import com.api.hackathon.model.UserBabyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser(UserBabyfoot user);
    void deleteAllByUser(UserBabyfoot user);
    @Modifying
    void deleteByBabyfootIdBabyfoot(Integer idBabyfoot);
    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE FUNCTION('DATE', b.dateBooking) = CURRENT_DATE")
    long countToday();
}