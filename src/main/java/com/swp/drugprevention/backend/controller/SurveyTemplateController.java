package com.swp.drugprevention.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp.drugprevention.backend.io.request.SubmitSurveyRequest;
import com.swp.drugprevention.backend.io.response.SurveyResponse;
import com.swp.drugprevention.backend.io.response.SurveyResultResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.service.surveyService.SurveyService;
import com.swp.drugprevention.backend.service.surveyService.SurveyTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/survey-template")
@RequiredArgsConstructor
public class SurveyTemplateController {

    private final SurveyTemplateService service;
    private final UserRepository userRepository;
    private final SurveyService surveyService;
    @PostMapping("/start")
    public ResponseEntity<?> startSurvey(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Survey survey = surveyService.startSurvey(user);
        SurveyResponse response = surveyService.toResponseDTO(survey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/survey/{surveyId}/submit")
    public ResponseEntity<?> submitSurvey(@PathVariable Integer surveyId,
                                          @RequestBody SubmitSurveyRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Controller - UserDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            System.out.println("Controller - User not found: " + userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Optional<Survey> optionalSurvey = surveyService.findSurveyById(surveyId);
        if (optionalSurvey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey not found.");
        }

        Survey survey = optionalSurvey.get();
        if (!survey.getUser().getUserId().equals(user.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to submit this survey.");
        }

        surveyService.submitSurvey(survey, request);

        // Tạo response để trả về kết quả và khuyến nghị
        SurveyResultResponse result = new SurveyResultResponse(
               survey.getTotalScore(), survey.getRecommendation()
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/my")
    public ResponseEntity<List<SurveyResponse>> getMySurveys(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SurveyResponse> responses = surveyService.getSurveysByUser(user).stream()
                .map(surveyService::toResponseDTO)
                .toList();

        return ResponseEntity.ok(responses);
    }


}
