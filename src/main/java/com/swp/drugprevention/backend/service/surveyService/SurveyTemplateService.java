package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.model.survey.SurveyOption;
import com.swp.drugprevention.backend.model.survey.SurveyQuestion;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyTemplateService {

    private final SurveyTemplateRepository templateRepo;

    public List<SurveyTemplate> getAllTemplates() {
        return templateRepo.findAll();
    }

    public SurveyTemplate getTemplateById(Integer id) {
        return templateRepo.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
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
        return templateRepo.save(template);
    }

    public SurveyTemplate updateTemplate(Integer id, SurveyTemplate updated) {
        SurveyTemplate existing = getTemplateById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setSurveyType(updated.getSurveyType());
        existing.getQuestions().clear();
        if (updated.getQuestions() != null) {
            for (SurveyQuestion q : updated.getQuestions()) {
                q.setTemplate(existing);
                if (q.getOptions() != null) {
                    for (SurveyOption o : q.getOptions()) {
                        o.setQuestion(q);
                    }
                }
                existing.getQuestions().add(q);
            }
        }
        return templateRepo.save(existing);
    }

    public void deleteTemplate(Integer id) {
        templateRepo.deleteById(id);
    }
}
