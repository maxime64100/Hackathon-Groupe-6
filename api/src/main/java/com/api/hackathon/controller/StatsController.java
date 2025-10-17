package com.api.hackathon.controller;

import com.api.hackathon.repository.BabyfootRepository;
import com.api.hackathon.repository.BookingRepository;
import com.api.hackathon.repository.UserBabyfootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final UserBabyfootRepository userRepo;
    private final BabyfootRepository babyfootRepo;
    private final BookingRepository bookingRepo;

    @GetMapping
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("babyfootsActifs", babyfootRepo.countByUsable(true));
        stats.put("usersActifs", userRepo.count());
        stats.put("reservationsAujourdhui", bookingRepo.countToday());
        stats.put("enMaintenance", babyfootRepo.countByStatutBabyfoot("maintenance"));

        return stats;
    }
}
