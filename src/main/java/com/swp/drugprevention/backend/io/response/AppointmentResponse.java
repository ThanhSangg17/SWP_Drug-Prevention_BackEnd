package com.swp.drugprevention.backend.io.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Integer appointmentId;
    private LocalDate date;
    private java.sql.Time time;
    private String status;
    private String location;
    private Integer userId;
    private Integer consultantId;
}
