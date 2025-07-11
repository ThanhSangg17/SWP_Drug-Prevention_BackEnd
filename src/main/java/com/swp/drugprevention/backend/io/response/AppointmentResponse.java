package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.enums.ConsultationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Integer appointmentId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ConsultationStatus status;
    private String location;
    private Integer userId;
    private Integer consultantId;
}
