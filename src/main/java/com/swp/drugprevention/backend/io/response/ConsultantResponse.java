package com.swp.drugprevention.backend.io.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultantResponse {
    private Integer consultantId;
    private String name;
    private String email;
    private boolean availability;
    private String phone;
    private Integer yob;
    private String schedule;
    private String specialization;
}
