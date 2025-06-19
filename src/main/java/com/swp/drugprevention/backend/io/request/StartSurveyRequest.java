package com.swp.drugprevention.backend.io.request;

import lombok.Data;

@Data
public class StartSurveyRequest {
    private Integer templateId;
    private String surveyType; // "Pre", "Post", "ASSIST", v.v.
    private Integer programId; // có thể null
    private Integer courseId;  // có thể null
}
