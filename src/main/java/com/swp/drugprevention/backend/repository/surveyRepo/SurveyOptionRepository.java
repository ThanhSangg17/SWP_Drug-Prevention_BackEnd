package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.survey.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Integer> {
    Optional<SurveyOption> findByQuestionQuestionIdAndValue(Integer questionId, String value);
}
