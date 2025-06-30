package com.swp.drugprevention.backend.io.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateConsultantProfileRequest {
    private String name;
    private String specialization;
    private String phone;
    private Integer yob;
}
