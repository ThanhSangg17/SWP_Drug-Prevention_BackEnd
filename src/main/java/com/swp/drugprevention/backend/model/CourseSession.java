package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CourseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sessionIndex;

    private LocalDateTime sessionDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private OfflineCourse course;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(Integer sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public OfflineCourse getCourse() {
        return course;
    }

    public void setCourse(OfflineCourse course) {
        this.course = course;
    }
}
