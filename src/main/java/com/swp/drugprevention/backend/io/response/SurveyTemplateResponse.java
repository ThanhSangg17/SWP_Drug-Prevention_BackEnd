package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.enums.GenderGroup;
import com.swp.drugprevention.backend.enums.RiskLevel;
import com.swp.drugprevention.backend.enums.SurveyType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyTemplateResponse {
    private Integer templateId;

    private String name;

    private String description;

    private SurveyType surveyType;

    private AgeGroup ageGroup;

    private GenderGroup genderGroup;

    private RiskLevel riskLevel;
}
