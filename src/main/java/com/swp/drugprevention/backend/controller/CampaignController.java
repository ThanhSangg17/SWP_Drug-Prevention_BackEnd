package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.request.CampaignImportRequest;
import com.swp.drugprevention.backend.io.request.CampaignSubmitRequest;
import com.swp.drugprevention.backend.model.campaign.Campaign;
import com.swp.drugprevention.backend.model.campaign.CampaignResponse;
import com.swp.drugprevention.backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Campaign campaign) {
        Campaign saved = campaignService.createCampaign(campaign);
        return ResponseEntity.ok("Created campaign with id: " + saved.getId());
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
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submit(@PathVariable Integer campaignId,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody CampaignSubmitRequest request) {
        if (request == null || request.getAnswers() == null || request.getAnswers().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Bạn chưa trả lời câu hỏi nào.");
        }

        try {
            CampaignResponse result = campaignService.submitSurvey(campaignId, userDetails.getUsername(), request);
            return ResponseEntity.ok("✅ Đã gửi khảo sát thành công! Điểm số: " + result.getTotalScore());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("🚨 Lỗi khi xử lý khảo sát: " + e.getMessage());
        }
    }

    @PostMapping("/import")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importCampaign(@RequestBody CampaignImportRequest request) {
        Campaign created = campaignService.importCampaign(request);
        return ResponseEntity.ok("Campaign created with id: " + created.getId());
    }

}

