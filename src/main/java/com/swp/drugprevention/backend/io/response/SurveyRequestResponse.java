package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SurveyRequestResponse {

    private Long id;
    private Integer userId;
    private Integer templateId;
    private String reason;
    private LocalDateTime requestDate;
    private String status;
    private String rejectionReason;
    public SurveyRequestResponse(Long id, Integer userId, Integer templateId, String reason, LocalDateTime requestDate, String status, String rejectionReason) {
        this.id = id;
        this.userId = userId;
        this.templateId = templateId;
        this.reason = reason;
        this.requestDate = requestDate;
        this.status = status;
        this.rejectionReason = "REJECTED".equals(status) ? rejectionReason : null;
    }
}