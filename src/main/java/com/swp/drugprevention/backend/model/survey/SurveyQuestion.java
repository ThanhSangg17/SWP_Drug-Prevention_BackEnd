package com.swp.drugprevention.backend.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "templateId")
    private SurveyTemplate template;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    // Có thể giữ lại nếu bạn muốn biểu diễn thang điểm tối đa
    private Integer maxScore;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SurveyOption> options;
}
