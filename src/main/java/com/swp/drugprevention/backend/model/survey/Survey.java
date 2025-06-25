package com.swp.drugprevention.backend.model.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.drugprevention.backend.model.Course;
import com.swp.drugprevention.backend.model.Program;
import com.swp.drugprevention.backend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Surveys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer surveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId", nullable = false)
    private SurveyTemplate template;

    private String surveyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProgramID")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourseID")
    private Course course;

    @Column(nullable = false)
    private LocalDate takenDate;

    private Integer totalScore;

    @Column(nullable = false)
    private String status; // Sử dụng enum để kiểm soát giá trị (Completed, InProgress)

    private String recommendation;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SurveyAnswer> answers = new ArrayList<>();

    @OneToOne(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private DashboardSurvey dashboardSurvey;
}