package com.swp.drugprevention.backend.io.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultantUpdateResponse {
    private Integer consultantId;
    private String name;
    private String phone;
    private Integer yob;
    private String specialization;
}
