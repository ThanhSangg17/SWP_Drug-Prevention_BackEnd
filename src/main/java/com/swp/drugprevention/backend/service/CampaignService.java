package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.CampaignImportRequest;
import com.swp.drugprevention.backend.io.request.CampaignSubmitRequest;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.campaign.*;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.campaignRepo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepo;
    private final CampaignQuestionRepository questionRepo;
    private final CampaignOptionRepository optionRepo;
    private final CampaignResponseRepository responseRepo;
    private final CampaignAnswerRepository answerRepo;

    private final UserRepository userRepo;

    public Campaign createCampaign(Campaign campaign) {
        return campaignRepo.save(campaign);
    }

    public List<Campaign> getAll() {
        return campaignRepo.findAll();
    }

    public Campaign getById(Integer id) {
        return campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public CampaignResponse submitSurvey(Integer campaignId, String userEmail, CampaignSubmitRequest request) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Campaign campaign = getById(campaignId);

        CampaignResponse response = CampaignResponse.builder()
                .user(user)
                .campaign(campaign)
                .submittedAt(LocalDate.now())
                .build();

        int totalScore = 0;
        List<CampaignAnswer> answers = new ArrayList<>();

        for (CampaignSubmitRequest.AnswerDTO dto : request.getAnswers()) {
            CampaignQuestion question = questionRepo.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Invalid question"));

            CampaignAnswer answer = CampaignAnswer.builder()
                    .question(question)
                    .answerText(dto.getAnswerText())
                    .response(response)
                    .build();

            // Nếu câu hỏi có option chọn, tính điểm nếu khớp
            if (question.getOptions() != null) {
                for (CampaignOption opt : question.getOptions()) {
                    if (opt.getText().equalsIgnoreCase(dto.getAnswerText())) {
                        totalScore += opt.getScore();
                        break;
                    }
                }
            }

            answers.add(answer);
        }

        response.setTotalScore(totalScore);
        response.setAnswers(answers);
        responseRepo.save(response);

        return response;
    }

    public Campaign importCampaign(CampaignImportRequest request) {
        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        List<CampaignQuestion> questions = new ArrayList<>();

        for (var qDto : request.getQuestions()) {
            CampaignQuestion question = CampaignQuestion.builder()
                    .content(qDto.getContent())
                    .type(qDto.getType())
                    .campaign(campaign)
                    .build();

            if (qDto.getOptions() != null) {
                List<CampaignOption> options = qDto.getOptions().stream()
                        .map(opt -> CampaignOption.builder()
                                .text(opt.getText())
                                .score(opt.getScore())
                                .question(question)
                                .build())
                        .toList();
                question.setOptions(options);
            }

            questions.add(question);
        }

        campaign.setQuestions(questions);
        return campaignRepo.save(campaign);
    }

}
