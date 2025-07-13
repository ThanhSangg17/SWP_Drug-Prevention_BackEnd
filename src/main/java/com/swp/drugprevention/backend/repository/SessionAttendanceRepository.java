package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.SessionAttendance;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
    Optional<SessionAttendance> findByUserAndSession(User user, CourseSession session);
    List<SessionAttendance> findBySession(CourseSession session);
}
