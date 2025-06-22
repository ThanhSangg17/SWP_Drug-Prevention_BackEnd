package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.io.response.DashboardSurveyResponse;
import com.swp.drugprevention.backend.model.survey.DashboardSurvey;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.repository.surveyRepo.DashboardSurveyRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public String generateRecommendation(int totalScore) {
        if (totalScore == 0) return "Không có nguy cơ nào. Tiếp tục duy trì lối sống lành mạnh bạn nhé.";
        if (totalScore < 10) return "Nguy cơ thấp. Hãy duy trì lối sống lành mạnh.";
        if (totalScore < 20) return "Nguy cơ trung bình. Nên tham khảo chuyên gia.";
        return "Nguy cơ cao. Khuyến nghị tư vấn tâm lý ngay.";
    }

    public List<DashboardSurveyResponse> getAll() {
        return dashboardSurveyRepository.findAll().stream()
                .map(dashboard -> DashboardSurveyResponse.builder()
                        .dbSurveyId(dashboard.getDbSurveyId())
                        .surveyId(dashboard.getSurvey().getSurveyId())
                        .recommendation(dashboard.getRecommendation())
                        .createdDate(dashboard.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    public DashboardSurveyResponse getById(Integer id) {
        return dashboardSurveyRepository.findById(id)
                .map(dashboard -> DashboardSurveyResponse.builder()
                        .dbSurveyId(dashboard.getDbSurveyId())
                        .surveyId(dashboard.getSurvey().getSurveyId())
                        .recommendation(dashboard.getRecommendation())
                        .createdDate(dashboard.getCreatedDate())
                        .build())
                .orElseThrow(() -> new RuntimeException("DashboardSurvey not found"));
    }

    public void delete(Integer id) {
        dashboardSurveyRepository.deleteById(id);
    }
}