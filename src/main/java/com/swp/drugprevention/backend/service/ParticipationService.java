package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.Participation;
import com.swp.drugprevention.backend.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository participationRepository;

    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }

    public Participation saveParticipation(Participation participation) {
        return participationRepository.save(participation);
    }
}
