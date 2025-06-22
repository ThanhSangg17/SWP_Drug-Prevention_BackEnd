package com.swp.drugprevention.backend.io.request;
import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.enums.GenderGroup;
import com.swp.drugprevention.backend.enums.RiskLevel;
import com.swp.drugprevention.backend.enums.SurveyType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyTemplateUpdateRequest {

    private String name;
    private String description;
    private SurveyType surveyType;
    private AgeGroup ageGroup;
    private GenderGroup genderGroup;
    private RiskLevel riskLevel;
    private List<SurveyQuestionRequest> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SurveyQuestionRequest {
        private Integer questionId; // Có thể null nếu thêm mới
        private String questionText;
        private String type;
        private List<SurveyOptionRequest> options;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SurveyOptionRequest {
        private Integer optionId; // Có thể null nếu thêm mới
        private String label;
        private Integer score;
        private String value;
    }
}