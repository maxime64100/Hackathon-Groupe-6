package com.api.hackathon.repository;

import com.api.hackathon.model.UserBabyfoot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserBabyfootRepository extends JpaRepository<UserBabyfoot, Integer> {
    Optional<UserBabyfoot> findByMail(String mail);
    boolean existsByMail(String mail);
}