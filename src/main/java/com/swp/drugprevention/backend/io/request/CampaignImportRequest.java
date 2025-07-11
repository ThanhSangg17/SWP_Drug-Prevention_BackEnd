package com.swp.drugprevention.backend.io.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignImportRequest {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<QuestionDTO> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionDTO {
        private String content;
        private String type; // MULTIPLE_CHOICE / TEXT
        private List<OptionDTO> options;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionDTO {
        private String text;
        private Integer score;
    }
}

