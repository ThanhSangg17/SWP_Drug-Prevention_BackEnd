package com.swp.drugprevention.backend.io.response;

import java.time.LocalDate;

public class FeedBackCourseResponse {
    private Integer feedbackId;
    private Integer userId;
    private Integer courseId;
    private LocalDate date;
    private String content;

    public FeedBackCourseResponse(Integer feedbackId, Integer userId, Integer courseId, LocalDate date, String content) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.courseId = courseId;
        this.date = date;
        this.content = content;
    }

    // Getters and Setters
    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}