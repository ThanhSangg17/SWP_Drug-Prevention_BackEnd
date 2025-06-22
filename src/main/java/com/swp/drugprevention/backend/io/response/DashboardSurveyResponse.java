package com.swp.drugprevention.backend.io.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSurveyResponse {

    private Integer dbSurveyId;

    private Integer surveyId;

    private String recommendation;

    private LocalDate createdDate;
}