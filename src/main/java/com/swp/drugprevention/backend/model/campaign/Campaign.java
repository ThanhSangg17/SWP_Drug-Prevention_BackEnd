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

    private Integer improveCount = 0;
    private Integer noImproveCount = 0;
    @Transient
    public Double getSuccessRatePercent() {
        int total = improveCount + noImproveCount;
        if (total == 0) return 0.0;
        return (improveCount * 100.0) / total;
    }

}
