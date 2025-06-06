package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
}
