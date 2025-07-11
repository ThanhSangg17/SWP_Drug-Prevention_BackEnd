package com.swp.drugprevention.backend.repository.campaignRepo;

import com.swp.drugprevention.backend.model.campaign.CampaignOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignOptionRepository extends JpaRepository<CampaignOption, Integer> {
}
