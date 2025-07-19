package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "AgeGroup", length = 50)
    private String ageGroup;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "StartTime")
    private java.sql.Time startTime;

    @Column(name = "EndTime")
    private java.sql.Time endTime;

    @Column(name = "Date")
    private java.sql.Date date;

    @Column(name = "Place", length = 255)
    private String place;

    @Column(name = "Link", length = 255)
    private String link;

    @Column(name = "Topic", length = 255)
    private String topic;

    @ManyToOne
    @JoinColumn(name = "Teacher", referencedColumnName = "ConsultantID")
    private Consultant teacher;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;
}