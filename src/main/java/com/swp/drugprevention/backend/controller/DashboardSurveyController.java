package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.survey.DashboardSurvey;
import com.swp.drugprevention.backend.service.surveyService.DashboardSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardSurveyController {

    private final DashboardSurveyService dashboardSurveyService;

    @GetMapping
    public ResponseEntity<List<DashboardSurvey>> getAll() {
        return ResponseEntity.ok(dashboardSurveyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardSurvey> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(dashboardSurveyService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        dashboardSurveyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
