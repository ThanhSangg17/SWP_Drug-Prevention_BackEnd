package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.enums.GenderGroup;
import com.swp.drugprevention.backend.enums.RiskLevel;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyTemplateRepository extends JpaRepository<SurveyTemplate, Integer> {
    List<SurveyTemplate> findByAgeGroupAndGenderGroupInAndRiskLevelIn(
            AgeGroup ageGroup,
            List<GenderGroup> genderGroups,
            List<RiskLevel> riskLevels
    );
    List<SurveyTemplate> findByAgeGroup(AgeGroup ageGroup);

    SurveyTemplate findByName(String name);

    List<SurveyTemplate> findAllByIsActiveTrue();
}
