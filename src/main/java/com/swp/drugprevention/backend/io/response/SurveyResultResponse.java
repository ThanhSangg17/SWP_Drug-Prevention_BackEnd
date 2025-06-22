package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultResponse {
    private Integer totalScore;
    private String recommendation;
}
