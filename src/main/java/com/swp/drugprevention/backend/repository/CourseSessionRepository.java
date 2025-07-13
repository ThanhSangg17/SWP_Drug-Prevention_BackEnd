package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.CourseSession;
import com.swp.drugprevention.backend.model.OfflineCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    List<CourseSession> findByCourse(OfflineCourse course);
}
