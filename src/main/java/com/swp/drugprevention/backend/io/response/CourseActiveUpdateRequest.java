package com.swp.drugprevention.backend.io.request;

import lombok.Data;

@Data
public class CourseActiveUpdateRequest {
    private Long courseId;
    private Boolean active;
}
