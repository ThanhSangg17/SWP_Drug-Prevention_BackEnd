package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
