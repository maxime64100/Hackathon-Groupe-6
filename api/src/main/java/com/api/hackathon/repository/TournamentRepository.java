package com.api.hackathon.repository;

import com.api.hackathon.model.Tournament;
import com.api.hackathon.model.UserBabyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    List<Tournament> findByUser(UserBabyfoot user);
    void deleteAllByUser(UserBabyfoot user);
}