package com.swp.drugprevention.backend.io;

import java.sql.Time;
import java.time.LocalDate;

public class AppointmentRequest {
    private LocalDate date;
    private java.sql.Time time;
    private String status;
    private String location;

    public AppointmentRequest() {
    }

    public AppointmentRequest(LocalDate date, Time time, String status, String location) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.location = location;
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
}
