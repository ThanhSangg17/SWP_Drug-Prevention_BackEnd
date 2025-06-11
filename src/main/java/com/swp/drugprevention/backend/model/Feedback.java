package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Feedbacks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @Column(name = "FeedbackID")
    private Integer feedbackId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", referencedColumnName = "ConsultantID")
    private Consultant consultant;

    @ManyToOne
    @JoinColumn(name = "CourseID", referencedColumnName = "CourseID")
    private Course course;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "Date")
    private java.sql.Date date;
}