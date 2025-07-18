package com.swp.drugprevention.backend.io.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionAnswerDTO {
    private String question;
    private String answer;
}
