package com.swp.drugprevention.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp.drugprevention.backend.io.request.SurveyTemplateUpdateRequest;
import com.swp.drugprevention.backend.io.response.DashboardSurveyResponse;
import com.swp.drugprevention.backend.io.response.SurveyRequestResponse;
import com.swp.drugprevention.backend.io.response.SurveyResponse;
import com.swp.drugprevention.backend.io.response.SurveyTemplateResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.model.survey.SurveyRequest;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyRequestRepository;
import com.swp.drugprevention.backend.service.surveyService.DashboardSurveyService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardSurveyController {

    private final DashboardSurveyService dashboardSurveyService;
    private final UserRepository userRepository;
    private final SurveyService surveyService;
    private final SurveyTemplateService service;
    private final SurveyRequestRepository surveyRequestRepository;

    // API để bật/tắt khảo sát
    //bấm 1 cái là survey template sẽ bật hoặc tắt
    @PutMapping("/template-toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleSurveyTemplate(@PathVariable Integer id) {
        try {
            SurveyTemplate template = service.toggleSurveyTemplateStatus(id);
            String status = template.isActive() ? "enabled" : "disabled";
            return ResponseEntity.ok("Survey template with id " + id + " has been " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey template not found: " + e.getMessage());
        }
    }

    @PostMapping("/import-survey")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importFromFile(@RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Bỏ qua các trường không tồn tại trong entity
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // Cho phép giá trị null cho các trường không bắt buộc
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            List<SurveyTemplate> templates = mapper.readValue(
                    file.getInputStream(), new TypeReference<List<SurveyTemplate>>() {}
            );

            for (SurveyTemplate template : templates) {
                // Kiểm tra các trường bắt buộc
                if (template.getName() == null || template.getSurveyType() == null ||
                        template.getAgeGroup() == null || template.getGenderGroup() == null ||
                        template.getRiskLevel() == null) {
                    return ResponseEntity.badRequest().body("Missing required fields in template: " + template.getName());
                }

                if (template.getQuestions() != null) {
                    for (var q : template.getQuestions()) {
                        q.setTemplate(template);
                        if (q.getOptions() != null) {
                            for (var o : q.getOptions()) {
                                o.setQuestion(q);
                            }
                        }
                    }
                }
            }

            List<SurveyTemplate> saved = templates.stream()
                    .map(service::createTemplate)
                    .toList();

            return ResponseEntity.ok().body("Imported " + saved.size() + " survey templates successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/getAll-surveys")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DashboardSurveyResponse>> getAllSurveys() {
        return ResponseEntity.ok(dashboardSurveyService.getAll());
    }

    @GetMapping("/surveyDetail/{surveyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SurveyResponse> getSurveyDetail(@PathVariable Integer surveyId,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        System.out.println("Controller - UserDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Survey> survey = surveyService.findSurveyById(surveyId);
        if (survey.isEmpty() || survey.get().getUser() == null || !survey.get().getUser().getUserId().equals(user.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(surveyService.toResponseDTO(survey.get()));
    }

    @GetMapping("/getAll-templates")
    public List<SurveyTemplateResponse> getAll() {
        return service.getAllTemplates();
    }

    // API mới: Lấy tất cả các template (bật/tắt) cho admin
    @GetMapping("/getAll-templates-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SurveyTemplateResponse>> getAllTemplatesForAdmin() {
        List<SurveyTemplate> templates = service.getAllTemplatesIncludingInactive();
        return ResponseEntity.ok(templates.stream().map(this::toResponse).collect(Collectors.toList()));
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
                .isActive(template.isActive())
                .build();
    }


    //Trong quá trình import survey nếu lỡ có sai sót gì thì có thể sửa lại -> Cũng hơi cần thiết
    @PutMapping("/template-update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody SurveyTemplateUpdateRequest request) {
        try {
            service.updateTemplate(id, request);
            return ResponseEntity.ok("Success to update survey template with id " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to update: " + e.getMessage());
        }
    }

    @DeleteMapping("/template-delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteTemplate(id);
            return ResponseEntity.ok().body("Survey template with id " + id + " deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-surveys-requests-resolved")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRequestResolved() {
        List<SurveyRequest> requests = surveyRequestRepository.findAll();
        if (requests.isEmpty()) {
            return ResponseEntity.ok("Hiện không có yêu cầu nào");
        }
        List<SurveyRequestResponse> responsesRequests = requests.stream()
                .map(surveyService::toResponse)
                .toList();
        return ResponseEntity.ok(responsesRequests);
    }
}