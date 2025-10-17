package com.api.hackathon.service;

import com.api.hackathon.model.Tournament;
import com.api.hackathon.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getById(Integer id) {
        return tournamentRepository.findById(id);
    }

    public Tournament create(Tournament t) {
        return tournamentRepository.save(t);
    }

    public Tournament update(Integer id, Tournament payload) {
        return tournamentRepository.findById(id)
                .map(existing -> {
                    existing.setDateTournament(payload.getDateTournament());
                    existing.setUser(payload.getUser()); // nullable OK
                    return tournamentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Tournament not found with id " + id));
    }

    public void delete(Integer id) {
        tournamentRepository.deleteById(id);
    }
}