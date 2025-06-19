package com.swp.drugprevention.backend.model.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.drugprevention.backend.model.Course;
import com.swp.drugprevention.backend.model.Program;
import com.swp.drugprevention.backend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "templateId")
    private SurveyTemplate template;

    private String surveyType;

    @ManyToOne
    @JoinColumn(name = "ProgramID")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

    private LocalDate takenDate;

    private Integer totalScore;

    private String status; // Completed, In Progress

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SurveyAnswer> answers;
}