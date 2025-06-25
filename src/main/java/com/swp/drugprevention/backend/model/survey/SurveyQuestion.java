package com.swp.drugprevention.backend.model.survey;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SurveyQuestions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId", nullable = false)
    private SurveyTemplate template;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    private Integer maxScore;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyOption> options = new ArrayList<>();
}
