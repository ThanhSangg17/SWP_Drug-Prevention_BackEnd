package com.swp.drugprevention.backend.io.response;

public class UserInfoResponse {
    private Integer userId;
    private String fullName;
    private String email;
    private Boolean isPresent;
    private Long courseId; // ✅ sửa đúng kiểu

    public UserInfoResponse() {}

    public UserInfoResponse(Integer userId, String fullName, String email, Boolean isPresent, Long courseId) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.isPresent = isPresent;
        this.courseId = courseId;
    }

    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
