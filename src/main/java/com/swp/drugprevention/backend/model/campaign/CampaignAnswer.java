package com.swp.drugprevention.backend.model.campaign;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    @ManyToOne
    private CampaignQuestion question;

    @ManyToOne
    private CampaignResponse response;
}

