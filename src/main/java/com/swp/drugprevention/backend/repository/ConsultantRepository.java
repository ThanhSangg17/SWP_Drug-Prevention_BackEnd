package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultantRepository extends JpaRepository<Consultant, Integer> {
}
