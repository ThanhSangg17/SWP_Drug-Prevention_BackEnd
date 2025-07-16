package com.swp.drugprevention.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.drugprevention.backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "Payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "OfflineCourseId")
    @JsonIgnore
    private OfflineCourse course;

    @Column(name = "Amount", nullable = false)
    private Double amount;

    @Column(name = "PaymentDate", nullable = false)
    private java.sql.Date paymentDate;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Column(name = "Status", length = 50)
    private PaymentStatus status;
}