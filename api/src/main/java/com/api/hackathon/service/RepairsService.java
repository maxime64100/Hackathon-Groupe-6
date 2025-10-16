package com.api.hackathon.service;

import com.api.hackathon.model.Repairs;
import com.api.hackathon.repository.RepairsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepairsService {

    private final RepairsRepository repairsRepository;

    public List<Repairs> getAll() {
        return repairsRepository.findAll();
    }

    public Optional<Repairs> getById(Integer id) {
        return repairsRepository.findById(id);
    }

    public Repairs create(Repairs r) {
        return repairsRepository.save(r);
    }

    public Repairs update(Integer id, Repairs payload) {
        return repairsRepository.findById(id)
                .map(existing -> {
                    existing.setStatusBabyfoot(payload.getStatusBabyfoot());
                    existing.setStartDateRepairs(payload.getStartDateRepairs());
                    existing.setEndDateRepairs(payload.getEndDateRepairs());
                    existing.setBabyfoot(payload.getBabyfoot());
                    return repairsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Repairs not found with id " + id));
    }

    public void delete(Integer id) {
        repairsRepository.deleteById(id);
    }
}