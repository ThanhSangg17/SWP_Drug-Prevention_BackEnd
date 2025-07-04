package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByUser_UserId(Integer userId);
    List<Feedback> findAllByConsultantIsNotNull();
    List<Feedback> findAllByCourseIsNotNull();
    List<Feedback> findAllByProgramIsNotNull();
}