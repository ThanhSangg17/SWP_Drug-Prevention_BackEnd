package com.swp.drugprevention.backend.io.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitSurveyRequest {

    private List<AnswerDTO> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerDTO {
        private Integer questionId;
        private String answerText;
    }
}
