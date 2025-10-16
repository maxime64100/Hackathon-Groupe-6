package com.api.hackathon.repository;

import com.api.hackathon.model.Babyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BabyfootRepository extends JpaRepository<Babyfoot, Integer> {
}