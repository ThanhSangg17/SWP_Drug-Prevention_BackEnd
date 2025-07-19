package com.swp.drugprevention.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
public class VnPayConfig {
    private String vnp_TmnCode;
    private String vnp_HashSecret;
    private String vnp_PayUrl;
    private String vnp_ReturnUrl;
}
