package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Surveys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    @Id
    @Column(name = "SurveyID")
    private Integer surveyId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @Column(name = "Type", length = 50)
    private String type;

    @Column(name = "Score")
    private Integer score;

    @Column(name = "Question", length = 255)
    private String question;

    @Column(name = "StartDate")
    private java.sql.Date startDate;

    @Column(name = "EndDate")
    private java.sql.Date endDate;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "TakenDate")
    private java.sql.Date takenDate;

    @Column(name = "Recommendation", columnDefinition = "TEXT")
    private String recommendation;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<DashBoardSurvey> dashBoardSurveys;
}