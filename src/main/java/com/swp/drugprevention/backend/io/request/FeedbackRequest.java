package com.swp.drugprevention.backend.io;

import jakarta.validation.constraints.NotBlank;

public class FeedbackRequest {
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private Integer userId;
    private Integer consultantId;
    private Integer courseId;
    private Integer programId;

    public FeedbackRequest() {
    }

    public FeedbackRequest(String content, Integer userId, Integer consultantId, Integer courseId, Integer programId) {
        this.content = content;
        this.userId = userId;
        this.consultantId = consultantId;
        this.courseId = courseId;
        this.programId = programId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }
}