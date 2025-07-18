package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.model.Consultant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfflineCourseResponse {
    private Long id;
    private String courseName;
    private Consultant consultant;
    private double price;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
    private Boolean active;
}
