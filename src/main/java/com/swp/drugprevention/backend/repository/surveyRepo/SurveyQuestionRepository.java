package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.survey.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {
}
