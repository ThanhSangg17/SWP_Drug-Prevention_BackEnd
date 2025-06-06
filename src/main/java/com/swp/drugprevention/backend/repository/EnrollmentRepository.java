package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
