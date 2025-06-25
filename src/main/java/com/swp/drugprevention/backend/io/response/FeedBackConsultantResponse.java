package com.swp.drugprevention.backend.io.response;

import java.time.LocalDate;

public class FeedBackConsultantResponse {
    private Integer feedbackId;
    private Integer userId;
    private Integer consultantId;
    private LocalDate date;
    private String content;

    public FeedBackConsultantResponse(Integer feedbackId, Integer userId, Integer consultantId, LocalDate date, String content) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.consultantId = consultantId;
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

    public Integer getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Integer consultantId) {
        this.consultantId = consultantId;
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