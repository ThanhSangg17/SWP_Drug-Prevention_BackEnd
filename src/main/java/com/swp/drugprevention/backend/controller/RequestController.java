package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.response.SurveyRequestResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.model.survey.SurveyRequest;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyRequestRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.SurveyTemplateRepository;
import com.swp.drugprevention.backend.service.surveyService.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/my-requests")
@RequiredArgsConstructor
public class RequestController {

    private final UserRepository userRepository;
    private final SurveyRequestRepository surveyRequestRepository;
    private final SurveyTemplateRepository surveyTemplateRepository;
    private final SurveyService surveyService;

    @GetMapping("/view-request-retake-survey")
    public ResponseEntity<List<SurveyRequestResponse>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SurveyRequest> requests;
        if (status != null && !status.isEmpty()) {
            requests = surveyRequestRepository.findByUserAndStatus(user, status);
        } else {
            requests = surveyRequestRepository.findByUser(user);
        }

        List<SurveyRequestResponse> responseDTOs = requests.stream()
                .map(surveyService::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping("/retake-survey")
    public ResponseEntity<?> requestAdditionalSurvey(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam Integer templateId,
                                                     @RequestParam(required = false) String reason) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SurveyTemplate template = surveyTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Survey template not found"));

        List<Survey> completedSurveys = surveyService.getSurveysByUserAndTemplate(user, template).stream()
                .filter(survey -> "Completed".equals(survey.getStatus()))
                .toList();

        if (completedSurveys.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Vui lòng hoàn thành bài khảo sát trước khi gửi yêu cầu làm lại.");
        }

        // Kiểm tra xem đã có yêu cầu PENDING cho template và user này chưa
        Optional<SurveyRequest> existingRequest = surveyRequestRepository.findByUserAndTemplateAndStatus(user, template, "PENDING");
        if (existingRequest.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Yêu cầu cho mẫu khảo sát này đã tồn tại và đang chờ xử lý.");
        }

        // Tạo và lưu yêu cầu mới
        SurveyRequest request = new SurveyRequest();
        request.setUser(user);
        request.setTemplate(template);
        request.setReason(reason);
        request.setRequestDate(LocalDateTime.now()); // Đảm bảo set requestDate nếu cần
        request.setStatus("PENDING"); // Đảm bảo set trạng thái mặc định
        SurveyRequest savedRequest = surveyRequestRepository.save(request);

        return ResponseEntity.ok("Yêu cầu đã được gửi thành công. Vui lòng chờ nhân viên xử lý.");
    }
}