package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
