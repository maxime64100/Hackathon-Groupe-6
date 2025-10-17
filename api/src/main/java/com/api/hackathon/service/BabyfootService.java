package com.api.hackathon.service;

import com.api.hackathon.model.Babyfoot;
import com.api.hackathon.repository.BabyfootRepository;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.RepairsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BabyfootService {

    private final BabyfootRepository babyfootRepo;
    private final BookingRepository bookingRepo;
    private final RepairsRepository repairsRepo;

    public List<Babyfoot> getAllBabyfoots() {
        return babyfootRepo.findAll();
    }

    public Optional<Babyfoot> getBabyfootById(Integer id) {
        return babyfootRepo.findById(id);
    }

    public Babyfoot createBabyfoot(Babyfoot babyfoot) {
        return babyfootRepo.save(babyfoot);
    }

    public Babyfoot updateBabyfoot(Integer id, Babyfoot updatedBabyfoot) {
        return babyfootRepo.findById(id)
                .map(existing -> {
                    existing.setPlace(updatedBabyfoot.getPlace());
                    existing.setStatutBabyfoot(updatedBabyfoot.getStatutBabyfoot());
                    existing.setUsable(updatedBabyfoot.getUsable());
                    return babyfootRepo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Babyfoot not found with id " + id));
    }

    @Transactional
    public void deleteBabyfoot(Integer id) {
        // Supprimer d'abord les réservations associées
        bookingRepo.deleteByBabyfootIdBabyfoot(id);
        repairsRepo.deleteByBabyfootIdBabyfoot(id);

        // Puis supprimer le babyfoot
        babyfootRepo.deleteById(id);
    }
}