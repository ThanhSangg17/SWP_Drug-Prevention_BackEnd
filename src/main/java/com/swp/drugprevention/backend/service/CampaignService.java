package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.CampaignImportRequest;
import com.swp.drugprevention.backend.io.request.CampaignSubmitRequest;
import com.swp.drugprevention.backend.io.response.CampaignSubmissionReviewDTO;
import com.swp.drugprevention.backend.io.response.QuestionAnswerDTO;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.campaign.*;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.campaignRepo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepo;
    private final CampaignQuestionRepository questionRepo;
    private final CampaignOptionRepository optionRepo;
    private final CampaignSubmissionRepository campaignSubmissionRepo;
    private final CampaignAnswerRepository answerRepo;
    private final UserRepository userRepo;

    public Campaign createCampaign(Campaign campaign) {
        return campaignRepo.save(campaign);
    }

    public List<CampaignSubmissionReviewDTO> getUserSubmissionsForReview(Integer campaignId, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Campaign campaign = getById(campaignId);

        List<CampaignSubmission> submissions = campaignSubmissionRepo
                .findByUserAndCampaignOrderBySubmittedAtAsc(user, campaign);

        return submissions.stream().map(sub -> {
            List<QuestionAnswerDTO> answers = sub.getAnswers().stream()
                    .map(ans -> new QuestionAnswerDTO(
                            ans.getQuestion().getContent(),
                            ans.getAnswerText()))
                    .toList();

            return new CampaignSubmissionReviewDTO(
                    sub.getAttemptNumber(),
                    sub.getSubmittedAt(),
                    sub.getTotalScore(),
                    answers
            );
        }).toList();
    }

    public List<Campaign> getAll() {
        return campaignRepo.findAll();
    }

    public Campaign getById(Integer id) {
        return campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public CampaignSubmission submitSurvey(Integer campaignId, Integer userId, CampaignSubmitRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Campaign campaign = getById(campaignId);

        List<CampaignSubmission> previousSubmissions = campaignSubmissionRepo.findByUserAndCampaignOrderBySubmittedAtAsc(user, campaign);

        if (previousSubmissions.size() >= 2) {
            throw new IllegalStateException("Bạn chỉ được làm tối đa 2 lần cho chiến dịch này.");
        }

        int attemptNumber = previousSubmissions.size() + 1;
        int totalScore = 0;

        List<CampaignAnswer> answers = new ArrayList<>();

        for (CampaignSubmitRequest.AnswerDTO dto : request.getAnswers()) {
            CampaignQuestion question = questionRepo.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Invalid question"));

            CampaignOption selectedOption = null;
            if ("MULTIPLE_CHOICE".equalsIgnoreCase(question.getType())) {
                selectedOption = question.getOptions().stream()
                        .filter(opt -> opt.getText().equalsIgnoreCase(dto.getAnswerText()))
                        .findFirst()
                        .orElse(null);

                if (selectedOption != null) {
                    totalScore += selectedOption.getScore();
                }
            }

            CampaignAnswer answer = CampaignAnswer.builder()
                    .question(question)
                    .answerText(dto.getAnswerText())
                    .build();
            answers.add(answer);
        }

        // 🧠 Tạo CampaignSubmission
        CampaignSubmission submission = CampaignSubmission.builder()
                .user(user)
                .campaign(campaign)
                .totalScore(totalScore)
                .attemptNumber(attemptNumber)
                .submittedAt(LocalDateTime.now())
                .build();

        for (CampaignAnswer answer : answers) {
            answer.setSubmission(submission);
        }

        submission.setAnswers(answers);
        campaignSubmissionRepo.save(submission);

        // 🔁 Kiểm tra điểm cải thiện nếu là lần thứ 2
        if (attemptNumber == 2 && previousSubmissions.size() == 1) {
            int previousScore = previousSubmissions.get(0).getTotalScore();
            if (totalScore > previousScore) {
                campaign.setImproveCount(campaign.getImproveCount() + 1);
            } else {
                campaign.setNoImproveCount(campaign.getNoImproveCount() + 1);
            }
            campaignRepo.save(campaign);
        }

        return submission;
    }

    public Campaign importCampaign(CampaignImportRequest request) {
        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .improveCount(0)
                .noImproveCount(0)
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
