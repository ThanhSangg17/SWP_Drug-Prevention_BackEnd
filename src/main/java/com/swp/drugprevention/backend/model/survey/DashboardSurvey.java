package com.swp.drugprevention.backend.model.survey;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "DashboardSurveys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbSurveyId;

    @OneToOne
    @JoinColumn(name = "surveyId")
    private Survey survey;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private LocalDate createdDate;
}
