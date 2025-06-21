package com.swp.drugprevention.backend.model.survey;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "templateId")
    private SurveyTemplate template;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    // Có thể giữ lại nếu bạn muốn biểu diễn thang điểm tối đa
    private Integer maxScore;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyOption> options;
}
