package com.swp.drugprevention.backend.model.campaign;

import com.swp.drugprevention.backend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campaign_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate submittedAt;

    private Integer totalScore;

    @ManyToOne
    private User user;

    @ManyToOne
    private Campaign campaign;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<CampaignAnswer> answers;
}

