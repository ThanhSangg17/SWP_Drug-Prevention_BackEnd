package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.survey.DashboardSurvey;
import com.swp.drugprevention.backend.service.surveyService.DashboardSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardSurveyController {

    private final DashboardSurveyService dashboardSurveyService;

    @GetMapping("/getAll-surveys")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DashboardSurvey>> getAllSurveys() {
        return ResponseEntity.ok(dashboardSurveyService.getAll());
    }

    @GetMapping("/surveys/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardSurvey> getSurveyById(@PathVariable Integer id) {
        return ResponseEntity.ok(dashboardSurveyService.getById(id));
    }

    @DeleteMapping("/surveys/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSurvey(@PathVariable Integer id) {
        dashboardSurveyService.delete(id);
        return ResponseEntity.ok().build();
    }
}