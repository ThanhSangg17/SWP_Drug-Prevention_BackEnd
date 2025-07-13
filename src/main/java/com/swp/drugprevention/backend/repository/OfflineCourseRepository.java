package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.OfflineCourse;
import com.swp.drugprevention.backend.model.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfflineCourseRepository extends JpaRepository<OfflineCourse, Long> {
    List<OfflineCourse> findByConsultant(Consultant consultant);
}
