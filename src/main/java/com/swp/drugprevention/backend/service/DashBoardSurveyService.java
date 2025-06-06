package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.DashBoardSurvey;
import com.swp.drugprevention.backend.repository.DashBoardSurveyRepository;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashBoardSurveyService {
    @Autowired
    private DashBoardSurveyRepository dashBoardSurveyRepository;

    public List<DashBoardSurvey> getAllDashBoardSurveys() {
        return dashBoardSurveyRepository.findAll();
    }

    public DashBoardSurvey saveDashBoardSurvey(DashBoardSurvey dashBoardSurvey) {
        return dashBoardSurveyRepository.save(dashBoardSurvey);
    }
}
