package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Participation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participation {
    @Id
    @Column(name = "ParticipationID")
    private Integer participationId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID")
    private Program program;

    @Column(name = "PreSurveyScore")
    private Integer preSurveyScore;

    @Column(name = "PostSurveyScore")
    private Integer postSurveyScore;
}