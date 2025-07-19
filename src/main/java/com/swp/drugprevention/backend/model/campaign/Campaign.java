package com.swp.drugprevention.backend.model.campaign;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignQuestion> questions;

    @Builder.Default
    private Integer improveCount = 0;

    @Builder.Default
    private Integer noImproveCount = 0;

    @Transient
    public Double getSuccessRatePercent() {
        int improve = improveCount != null ? improveCount : 0;
        int noImprove = noImproveCount != null ? noImproveCount : 0;
        int total = improve + noImprove;
        if (total == 0) return 0.0;
        return improve * 100.0 / total;
    }

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

}
