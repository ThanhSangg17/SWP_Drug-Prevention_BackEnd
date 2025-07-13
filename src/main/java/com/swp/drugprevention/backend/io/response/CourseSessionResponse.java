package com.swp.drugprevention.backend.io.response;

import java.time.LocalDateTime;

public class CourseSessionResponse {
    private Long sessionId;
    private Long courseId;           // ✅ thêm courseId
    private Integer sessionIndex;
    private LocalDateTime sessionDate;
    private Boolean isPresent;       // ✅ thêm isPresent nếu cần

    public CourseSessionResponse() {}

    // ✅ Constructor đúng số lượng tham số anh đang dùng
    public CourseSessionResponse(Long sessionId, Long courseId, Integer sessionIndex, LocalDateTime sessionDate) {
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.sessionIndex = sessionIndex;
        this.sessionDate = sessionDate;
    }

    // Getters & Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }
}
