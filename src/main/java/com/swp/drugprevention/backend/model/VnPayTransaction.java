package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vnpay_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPayTransaction {

    @Id
    private String vnpTxnRef; // Mã giao dịch (duy nhất)

    private String orderInfo;
    private String responseCode;
    private String transactionStatus;
    private String bankCode;
    private String payDate;
    private Integer amount;
    private String cardType;
}

