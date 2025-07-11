package com.swp.drugprevention.backend.repository.campaignRepo;

import com.swp.drugprevention.backend.model.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {}

