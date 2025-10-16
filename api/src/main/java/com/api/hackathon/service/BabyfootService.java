package com.api.hackathon.service;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.repository.BabyfootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BabyfootService {

    private final BabyfootRepository babyfootRepository;

    public List<Babyfoot> getAllBabyfoots() {
        return babyfootRepository.findAll();
    }

    public Optional<Babyfoot> getBabyfootById(Integer id) {
        return babyfootRepository.findById(id);
    }

    public Babyfoot createBabyfoot(Babyfoot babyfoot) {
        return babyfootRepository.save(babyfoot);
    }

    public Babyfoot updateBabyfoot(Integer id, Babyfoot updatedBabyfoot) {
        return babyfootRepository.findById(id)
                .map(existing -> {
                    existing.setPlace(updatedBabyfoot.getPlace());
                    existing.setStatutBabyfoot(updatedBabyfoot.getStatutBabyfoot());
                    existing.setUsable(updatedBabyfoot.getUsable());
                    return babyfootRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Babyfoot not found with id " + id));
    }

    public void deleteBabyfoot(Integer id) {
        babyfootRepository.deleteById(id);
    }
}