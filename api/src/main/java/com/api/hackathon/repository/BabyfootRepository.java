package com.api.hackathon.repository;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.model.Booking;
import com.api.hackathon.model.UserBabyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyfootRepository extends JpaRepository<Babyfoot, Integer> {
    long countByUsable(boolean usable);
    long countByStatutBabyfoot(String statutBabyfoot);
}