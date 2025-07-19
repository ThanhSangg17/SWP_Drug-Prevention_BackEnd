package com.swp.drugprevention.backend.io.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CampaignSubmissionReviewDTO {
    private Integer attemptNumber;
    private LocalDateTime submittedAt;
    private Integer totalScore;
    private List<QuestionAnswerDTO> answers;
}
