package com.swp.drugprevention.backend.io.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignSubmitRequest {

    @NotNull(message = "Danh sách câu trả lời không được null")
    @Size(min = 1, message = "Phải có ít nhất một câu trả lời")
    private List<@Valid AnswerDTO> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerDTO {
        @NotNull(message = "Câu hỏi không được null")
        private Integer questionId;

        @NotNull(message = "Bạn phải nhập câu trả lời")
        private String answerText;
    }
}

