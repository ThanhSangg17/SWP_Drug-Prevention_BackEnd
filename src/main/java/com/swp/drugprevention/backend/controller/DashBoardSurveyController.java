package com.swp.drugprevention.backend.controller;
import com.swp.drugprevention.backend.model.DashBoardSurvey;
import com.swp.drugprevention.backend.service.DashBoardSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard-surveys")
public class DashBoardSurveyController {
    @Autowired
    private DashBoardSurveyService dashBoardSurveyService;

    @GetMapping("/getAllDashBoardSurveys")
    public List<DashBoardSurvey> getAllDashBoardSurveys() {
        return dashBoardSurveyService.getAllDashBoardSurveys();
    }

    @PostMapping("/createDashBoardSurvey")
    public DashBoardSurvey createDashBoardSurvey(@RequestBody DashBoardSurvey dashBoardSurvey) {
        return dashBoardSurveyService.saveDashBoardSurvey(dashBoardSurvey);
    }
}
