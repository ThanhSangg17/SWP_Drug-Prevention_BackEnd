package com.swp.drugprevention.backend.io.response;

import java.time.LocalDate;

public class FeedBackProgramResponse {
    private Integer feedbackId;
    private Integer userId;
    private Integer programId;
    private LocalDate date;
    private String content;

    public FeedBackProgramResponse(Integer feedbackId, Integer userId, Integer programId, LocalDate date, String content) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.programId = programId;
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

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
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