package com.swp.drugprevention.backend.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingRegistration {
    private ProfileRequest profileRequest;
    private String otp;
    private long expiryTime;
}
