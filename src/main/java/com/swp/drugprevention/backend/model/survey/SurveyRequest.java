package com.swp.drugprevention.backend.model.survey;
import com.swp.drugprevention.backend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_requests")
public class SurveyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private SurveyTemplate template;

    private String reason; // Lý do yêu cầu
    private LocalDateTime requestDate;
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private String rejectionReason; // Lý do từ chối
    @PrePersist
    protected void onCreate() {
        requestDate = LocalDateTime.now();
        status = "PENDING";
    }
}
