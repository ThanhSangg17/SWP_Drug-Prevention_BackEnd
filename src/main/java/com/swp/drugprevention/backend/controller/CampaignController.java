package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.request.CampaignImportRequest;
import com.swp.drugprevention.backend.io.request.CampaignSubmitRequest;
import com.swp.drugprevention.backend.model.campaign.Campaign;
import com.swp.drugprevention.backend.model.campaign.CampaignSubmission;
import com.swp.drugprevention.backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;


    @PostMapping("/import")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> importCampaign(@RequestBody CampaignImportRequest request) {
        Campaign created = campaignService.importCampaign(request);
        return ResponseEntity.ok("✅ Chiến dịch được import thành công với ID: " + created.getId());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Campaign>> getAll() {
        return ResponseEntity.ok(campaignService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getOne(@PathVariable Integer id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }

    @PostMapping("/{campaignId}/submit")
    public ResponseEntity<?> submitCampaign(
            @PathVariable Integer campaignId,
            @RequestParam Integer userId,
            @RequestBody CampaignSubmitRequest request) {

        if (request == null || request.getAnswers() == null || request.getAnswers().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Bạn chưa trả lời câu hỏi nào.");
        }

        try {
            CampaignSubmission result = campaignService.submitSurvey(campaignId, userId, request);
            return ResponseEntity.ok(Map.of(
                    "totalScore", result.getTotalScore(),
                    "attemptNumber", result.getAttemptNumber(),
                    "submittedAt", result.getSubmittedAt(),
                    "message", "✅ Bạn đã hoàn thành khảo sát lần " + result.getAttemptNumber()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚨 Lỗi khi xử lý khảo sát: " + e.getMessage());
        }
    }
}
