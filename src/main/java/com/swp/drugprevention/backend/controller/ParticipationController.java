package com.swp.drugprevention.backend.controller;
import com.swp.drugprevention.backend.model.Participation;
import com.swp.drugprevention.backend.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participation")
public class ParticipationController {
    @Autowired
    private ParticipationService participationService;

    @GetMapping("/getAllParticipation")
    public List<Participation> getAllParticipation() {
        return participationService.getAllParticipations();
    }

    @PostMapping("/createParticipation")
    public Participation createParticipation(@RequestBody Participation participation) {
        return participationService.saveParticipation(participation);
    }
}
