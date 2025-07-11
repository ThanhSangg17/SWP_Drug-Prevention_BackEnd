package com.swp.drugprevention.backend.repository.campaignRepo;
import com.swp.drugprevention.backend.model.campaign.CampaignQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignQuestionRepository extends JpaRepository<CampaignQuestion, Integer> {}

