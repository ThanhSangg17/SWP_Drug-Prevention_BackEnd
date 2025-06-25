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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/staff/survey")
@RequiredArgsConstructor
public class StaffSurveyController {

    private final SurveyRequestRepository surveyRequestRepository;
    private final UserRepository userRepository;
    private final SurveyTemplateRepository surveyTemplateRepository;
    private final SurveyService surveyService;

    @GetMapping("/get-user-requests")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getPendingRequests(
            @RequestParam(required = false) String status) {
        List<SurveyRequest> requests;
        if (status != null && !status.isEmpty()) {
            requests = surveyRequestRepository.findByStatus(status);
        } else {
            requests = surveyRequestRepository.findByStatus("PENDING");
        }

        if (requests.isEmpty()) {
            return ResponseEntity.ok("Hiện không có yêu cầu nào");
        }

        List<SurveyRequestResponse> responseDTOs = requests.stream()
                .map(surveyService::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping("/request/{requestId}/approve")
    @PreAuthorize("hasRole('STAFF')")
    @Transactional
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        SurveyRequest request = surveyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại"));

        if (!"PENDING".equals(request.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Yêu cầu này đã được xử lý trước đó.");
        }

        // Cập nhật trạng thái
        request.setStatus("APPROVED");
        surveyRequestRepository.save(request);

        // Xóa bài khảo sát gần nhất của User với cùng template
        User user = request.getUser();
        SurveyTemplate template = request.getTemplate();
        List<Survey> userSurveys = surveyService.getSurveysByUserAndTemplate(user, template);
        if (!userSurveys.isEmpty()) {
            Survey latestSurvey = userSurveys.get(0); // Bài gần nhất
            surveyService.deleteSurvey(latestSurvey);
        }

        return ResponseEntity.ok("Yêu cầu đã được chấp thuận. Bài khảo sát gần nhất đã bị xóa. Người dùng có thể làm thêm bài khảo sát.");
    }

    @PostMapping("/request/{requestId}/reject")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId,
                                           @RequestParam(required = false) String rejectionReason) {
        SurveyRequest request = surveyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại"));

        if (!"PENDING".equals(request.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Yêu cầu đã được xử lý.");
        }

        request.setStatus("REJECTED");
        request.setRejectionReason(rejectionReason); // Gán lý do từ chối
        surveyRequestRepository.save(request);

        return ResponseEntity.ok("Yêu cầu đã bị từ chối. Lý do: " + (rejectionReason != null ? rejectionReason : "Không có lý do"));
    }
}
