package com.swp.drugprevention.backend.model.survey;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SurveyAnswers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyId", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId", nullable = false)
    private SurveyQuestion question;

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String answerText;
}
