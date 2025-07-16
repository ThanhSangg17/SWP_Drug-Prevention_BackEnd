package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Integer paymentId;

    private Integer userId;

    private Long courseId;

    private Double amount;

    private String paymentMethod;

    private PaymentStatus status;

    private String qrCodeUrl;

    private String message;

}
