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

    private Double amount;

    private String paymentMethod;

    private PaymentStatus status;

    private String qrCodeUrl; // nếu dùng QR / VNPAY

    private String message; // mô tả thêm nếu cần

}
