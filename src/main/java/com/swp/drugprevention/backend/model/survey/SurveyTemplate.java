package com.swp.drugprevention.backend.model.survey;

import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.enums.GenderGroup;
import com.swp.drugprevention.backend.enums.RiskLevel;
import com.swp.drugprevention.backend.enums.SurveyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SurveyTemplates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SurveyType surveyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderGroup genderGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<Survey> surveys = new ArrayList<>();

}
