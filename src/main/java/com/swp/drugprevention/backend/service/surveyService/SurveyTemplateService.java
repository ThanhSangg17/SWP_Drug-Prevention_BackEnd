package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.io.request.SurveyTemplateUpdateRequest;
import com.swp.drugprevention.backend.io.response.SurveyTemplateResponse;
import com.swp.drugprevention.backend.model.survey.SurveyOption;
import com.swp.drugprevention.backend.model.survey.SurveyQuestion;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyTemplateService {

    private final SurveyTemplateRepository templateRepo;

    public List<SurveyTemplateResponse> getAllTemplates() {
        return templateRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SurveyTemplateResponse toResponse(SurveyTemplate template) {
        return SurveyTemplateResponse.builder()
                .templateId(template.getTemplateId())
                .name(template.getName())
                .description(template.getDescription())
                .surveyType(template.getSurveyType())
                .ageGroup(template.getAgeGroup())
                .genderGroup(template.getGenderGroup())
                .riskLevel(template.getRiskLevel())
                .build();
    }

    public SurveyTemplateResponse getTemplateById(Integer id) {
        return templateRepo.findById(id).map(this::toResponse).orElseThrow(() -> new RuntimeException("Survey template not found with id: " + id));
    }

    public SurveyTemplate createTemplate(SurveyTemplate template) {
        if (template.getQuestions() != null) {
            for (SurveyQuestion q : template.getQuestions()) {
                q.setTemplate(template);
                if (q.getOptions() != null) {
                    for (SurveyOption o : q.getOptions()) {
                        o.setQuestion(q);
                    }
                }
            }
        }

        //check if the template already exists
        SurveyTemplate existing = templateRepo.findByName(template.getName());
        if (existing != null) {
            throw new IllegalStateException("Survey template with name '" + template.getName() + "' already exists.");
        }
        return templateRepo.save(template);
    }

    public SurveyTemplateResponse updateTemplate(Integer id, SurveyTemplateUpdateRequest request) {
        SurveyTemplate existing = templateRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey template not found with id: " + id));

        // Cập nhật các trường cơ bản
        existing.setName(request.getName() != null ? request.getName() : existing.getName());
        existing.setDescription(request.getDescription() != null ? request.getDescription() : existing.getDescription());
        existing.setSurveyType(request.getSurveyType() != null ? request.getSurveyType() : existing.getSurveyType());
        existing.setAgeGroup(request.getAgeGroup() != null ? request.getAgeGroup() : existing.getAgeGroup());
        existing.setGenderGroup(request.getGenderGroup() != null ? request.getGenderGroup() : existing.getGenderGroup());
        existing.setRiskLevel(request.getRiskLevel() != null ? request.getRiskLevel() : existing.getRiskLevel());

        // Xử lý questions
        if (request.getQuestions() != null) {
            existing.getQuestions().clear(); // Xóa các question cũ
            for (SurveyTemplateUpdateRequest.SurveyQuestionRequest qRequest : request.getQuestions()) {
                SurveyQuestion question = (qRequest.getQuestionId() != null)
                        ? existing.getQuestions().stream()
                        .filter(q -> q.getQuestionId().equals(qRequest.getQuestionId()))
                        .findFirst()
                        .orElse(new SurveyQuestion())
                        : new SurveyQuestion();
                question.setQuestionText(qRequest.getQuestionText());
                question.setTemplate(existing);

                if (qRequest.getOptions() != null) {
                    question.getOptions().clear();
                    for (SurveyTemplateUpdateRequest.SurveyOptionRequest oRequest : qRequest.getOptions()) {
                        SurveyOption option = (oRequest.getOptionId() != null)
                                ? question.getOptions().stream()
                                .filter(o -> o.getOptionId().equals(oRequest.getOptionId()))
                                .findFirst()
                                .orElse(new SurveyOption())
                                : new SurveyOption();
                        option.setLabel(oRequest.getLabel());
                        option.setScore(oRequest.getScore());
                        option.setValue(oRequest.getValue());
                        option.setQuestion(question);
                        question.getOptions().add(option);
                    }
                }
                existing.getQuestions().add(question);
            }
        }

        SurveyTemplate saved = templateRepo.save(existing);
        return toResponse(saved); // Sử dụng phương thức toResponse đã định nghĩa
    }

    public void deleteTemplate(Integer id) {
        SurveyTemplate template = templateRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey template not found with id: " + id));
        templateRepo.delete(template);
    }
}
