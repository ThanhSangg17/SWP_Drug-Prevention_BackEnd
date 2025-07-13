package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Enrollment;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.OfflineCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    boolean existsByUserAndOfflineCourse(User user, OfflineCourse offlineCourse);

    List<Enrollment> findByOfflineCourseIn(List<OfflineCourse> offlineCourses);
    Optional<Enrollment> findByUserAndOfflineCourse(User user, OfflineCourse course);
    List<Enrollment> findByUser(User user);


}
