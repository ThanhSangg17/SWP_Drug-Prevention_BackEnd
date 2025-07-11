package com.swp.drugprevention.backend.io.request;

import com.swp.drugprevention.backend.enums.ConsultationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;
    private LocalTime endTime;
    @NotBlank(message = "Location cannot be empty")
    private String location;
    private Integer userId;
    private Integer consultantId;
}
