package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.Survey;
import com.swp.drugprevention.backend.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }
    public Survey saveSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }
}
