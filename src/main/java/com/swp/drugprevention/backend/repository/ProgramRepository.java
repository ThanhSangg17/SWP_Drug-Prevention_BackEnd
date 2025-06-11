package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
