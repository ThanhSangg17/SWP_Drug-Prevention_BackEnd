package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.survey.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Integer> {
}
