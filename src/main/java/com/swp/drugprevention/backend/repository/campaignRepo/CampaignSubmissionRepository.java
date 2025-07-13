package com.swp.drugprevention.backend.repository.campaignRepo;

import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.campaign.Campaign;
import com.swp.drugprevention.backend.model.campaign.CampaignSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignSubmissionRepository extends JpaRepository<CampaignSubmission, Long> {
    List<CampaignSubmission> findByUserAndCampaignOrderBySubmittedAtAsc(User user, Campaign campaign);
    List<CampaignSubmission> findByCampaign(Campaign campaign);
}
