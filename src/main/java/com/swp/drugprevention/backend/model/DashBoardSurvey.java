package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DashBoardSurveys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardSurvey {
    @Id
    @Column(name = "DBSurveyID")
    private Integer dbSurveyId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "SurveyID", referencedColumnName = "SurveyID")
    private Survey survey;

    @Column(name = "SurveyType", length = 50)
    private String surveyType;

    @Column(name = "TotalScore")
    private Integer totalScore;

    @Column(name = "TakenDate")
    private java.sql.Date takenDate;

    @Column(name = "Recommendation", columnDefinition = "TEXT")
    private String recommendation;
}