package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.survey.DashboardSurvey;
import com.swp.drugprevention.backend.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardSurveyRepository extends JpaRepository<DashboardSurvey, Integer> {
    Optional<DashboardSurvey> findBySurvey(Survey survey);
}
