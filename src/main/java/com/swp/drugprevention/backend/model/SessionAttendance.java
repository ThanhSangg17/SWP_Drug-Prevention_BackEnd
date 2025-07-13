package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;

@Entity
public class SessionAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private CourseSession session;

    private Boolean isPresent;

    // Getters & Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public CourseSession getSession() { return session; }

    public void setSession(CourseSession session) { this.session = session; }

    public Boolean getIsPresent() { return isPresent; }

    public void setIsPresent(Boolean isPresent) { this.isPresent = isPresent; }
}
