package com.swp.drugprevention.backend.model.campaign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    private Integer score;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private CampaignQuestion question;
}

