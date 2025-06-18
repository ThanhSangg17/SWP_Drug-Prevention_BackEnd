package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    List<Survey> findByUser(User user);
}
