package com.swp.drugprevention.backend.io.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
public class AppointmentResponse {
    private LocalDate date;
    private java.sql.Time time;
    private String status;
    private String location;
    private Integer userId;
    private Integer consultantId;

    public AppointmentResponse(LocalDate date, Time time, String status, String location, Integer userId, Integer consultantId) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.location = location;
        this.userId = userId;
        this.consultantId = consultantId;
    }

    public AppointmentResponse() {
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
