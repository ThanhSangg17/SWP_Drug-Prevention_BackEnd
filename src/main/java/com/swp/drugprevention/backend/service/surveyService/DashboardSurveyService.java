package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.model.survey.DashboardSurvey;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.repository.surveyRepo.DashboardSurveyRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardSurveyService {

    private final DashboardSurveyRepository dashboardSurveyRepository;
    private final SurveyRepository surveyRepository;

    public DashboardSurvey createIfNotExists(Survey survey) {
        return dashboardSurveyRepository.findBySurvey(survey).orElseGet(() -> {
            String recommendation = generateRecommendation(survey.getTotalScore());
            DashboardSurvey dashboard = DashboardSurvey.builder()
                    .survey(survey)
                    .createdDate(LocalDate.now())
                    .recommendation(recommendation)
                    .build();
            return dashboardSurveyRepository.save(dashboard);
        });
    }

    private String generateRecommendation(Integer score) {
        if (score == null) return "No data available.";
        if (score < 10) return "Low risk. Keep up the good habits.";
        if (score < 20) return "Medium risk. Consider reviewing your behaviors.";
        return "High risk. Seek professional counseling.";
    }

    public List<DashboardSurvey> getAll() {
        return dashboardSurveyRepository.findAll();
    }

    public DashboardSurvey getById(Integer id) {
        return dashboardSurveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DashboardSurvey not found"));
    }

    public void delete(Integer id) {
        dashboardSurveyRepository.deleteById(id);
    }
}
