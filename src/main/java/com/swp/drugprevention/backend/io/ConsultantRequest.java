package com.swp.drugprevention.backend.io.request;

public class ConsultantRequest {
    private String name;
    private String email;
    private String specialization;
    private String availability;
    private String schedule;

    public ConsultantRequest() {
    }

    public ConsultantRequest(String name, String email, String specialization, String availability, String schedule) {
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.availability = availability;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
