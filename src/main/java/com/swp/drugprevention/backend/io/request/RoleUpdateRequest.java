package com.swp.drugprevention.backend.io.request;


import com.swp.drugprevention.backend.enums.RoleName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateRequest {
    private RoleName roleName;
}
