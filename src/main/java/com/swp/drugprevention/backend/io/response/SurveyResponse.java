package com.swp.drugprevention.backend.io.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    private Integer surveyId;
    private String surveyType;
    private String status;
    private LocalDate takenDate;
    private int totalScore;
    private String recommendation;
    private ProfileResponse user; // dùng DTO ProfileResponse
    private List<SurveyAnswerDTO> answers;

    @Data
    public static class SurveyAnswerDTO {
        private Integer questionId;
        private String questionText;
        private String answerText;
        private int score;
    }
}