package com.swp.drugprevention.backend.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SurveyOptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optionId;

    private String label;

    private Integer score;

    // Thêm dòng này
    private String value;

    @ManyToOne
    @JoinColumn(name = "questionId")
    @JsonIgnore
    private SurveyQuestion question;
}
