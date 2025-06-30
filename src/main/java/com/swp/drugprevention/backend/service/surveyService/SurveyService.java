package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.io.request.StartSurveyRequest;
import com.swp.drugprevention.backend.io.request.SubmitSurveyRequest;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import com.swp.drugprevention.backend.io.response.SurveyRequestResponse;
import com.swp.drugprevention.backend.io.response.SurveyResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.*;
import com.swp.drugprevention.backend.repository.CourseRepository;
import com.swp.drugprevention.backend.repository.ProgramRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyTemplateRepository surveyTemplateRepository;
    private final ProgramRepository programRepository;
    private final CourseRepository courseRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final DashboardSurveyRepository dashboardSurveyRepository;
    private final DashboardSurveyService dashboardSurveyService;
    private final SurveyOptionRepository surveyOptionRepository;
    private final SurveyRequestRepository surveyRequestRepository;

    @Transactional
    public Survey startSurvey(User user, SurveyTemplate template) {
        if (template == null) {
            throw new IllegalStateException("No survey template provided.");
        }

        // Kiểm tra bài khảo sát chưa hoàn thành
        List<Survey> incompleteSurveys = surveyRepository.findByUserAndTemplateAndStatusNot(user, template, "Completed");
        if (!incompleteSurveys.isEmpty()) {
            throw new IllegalStateException("Bạn đã có bài khảo sát chưa hoàn thành. Vui lòng hoàn thành trước khi bắt đầu mới.");
        }

        // Kiểm tra yêu cầu được chấp thuận
        Optional<SurveyRequest> approvedRequest = surveyRequestRepository.findByUserAndTemplateAndStatus(user, template, "APPROVED");

        // Tìm bài khảo sát gần nhất
        Optional<Survey> recentSurvey = surveyRepository.findFirstByUserAndTemplateOrderByTakenDateDesc(user, template);

        // Nếu có yêu cầu được chấp thuận, xóa yêu cầu để ngăn tái sử dụng
        if (approvedRequest.isPresent()) {
            // Xóa yêu cầu APPROVED vì bài khảo sát gần nhất đã được xóa trong approveRequest
            surveyRequestRepository.delete(approvedRequest.get());
        } else {
            // Nếu không có yêu cầu được chấp thuận, kiểm tra quy tắc 7 ngày
            if (recentSurvey.isPresent()) {
                Survey lastSurvey = recentSurvey.get();
                LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
                if (!sevenDaysAgo.isAfter(lastSurvey.getTakenDate())) {
                    throw new IllegalStateException("Bạn chỉ có thể làm lại khảo sát này sau 7 ngày kể từ lần trước.");
                }
            }
        }

        // Tạo bài khảo sát mới
        Survey survey = Survey.builder()
                .user(user)
                .template(template)
                .surveyType(template.getSurveyType().name())
                .takenDate(LocalDate.now())
                .status("In Progress")
                .program(null)
                .course(null)
                .totalScore(0)
                .build();

        Survey savedSurvey = surveyRepository.save(survey);

        List<SurveyAnswer> answers = new ArrayList<>();
        if (template.getQuestions() != null && !template.getQuestions().isEmpty()) {
            answers = template.getQuestions().stream()
                    .map(q -> SurveyAnswer.builder()
                            .survey(savedSurvey)
                            .question(q)
                            .score(0)
                            .answerText("")
                            .build())
                    .toList();
            surveyAnswerRepository.saveAll(answers);
        }

        savedSurvey.setAnswers(answers);

        return savedSurvey;
    }

    public void submitSurvey(Survey survey, SubmitSurveyRequest request) {
        if ("Completed".equalsIgnoreCase(survey.getStatus())) {
            throw new IllegalStateException("Bài khảo sát đã được hoàn thành trước đó.");
        }
        Map<Integer, SubmitSurveyRequest.AnswerDTO> answerMap = new HashMap<>();
        for (SubmitSurveyRequest.AnswerDTO dto : request.getAnswers()) {
            answerMap.put(dto.getQuestionId(), dto);
        }

        int totalScore = 0;
        String surveyType = survey.getSurveyType();

        for (SurveyAnswer answer : survey.getAnswers()) {
            SubmitSurveyRequest.AnswerDTO dto = answerMap.get(answer.getQuestion().getQuestionId());
            if (dto != null) {
                answer.setAnswerText(dto.getAnswerText());
                if ("CRAFFT".equalsIgnoreCase(surveyType)) {
                    // Tính điểm cho CRAFFT
                    if ("YES".equalsIgnoreCase(dto.getAnswerText())) {
                        answer.setScore(1);
                        totalScore += 1;
                    } else if ("NO".equalsIgnoreCase(dto.getAnswerText())) {
                        answer.setScore(0);
                    } else {
                        answer.setScore(0);
                        System.out.println("Warning: Invalid answer for CRAFFT questionId: " +
                                answer.getQuestion().getQuestionId() + ", answerText: " + dto.getAnswerText());
                    }
                } else {
                    // Tính điểm cho ASSIST
                    Integer questionScore = getOptionScore(answer.getQuestion().getQuestionId(), dto.getAnswerText());
                    if (questionScore != null) {
                        answer.setScore(questionScore);
                        totalScore += questionScore;
                    } else {
                        answer.setScore(0);
                        System.out.println("Warning: No score found for questionId: " +
                                answer.getQuestion().getQuestionId() + ", answerText: " + dto.getAnswerText());
                    }
                }
            }
        }

        survey.setStatus("Completed");
        survey.setTotalScore(totalScore);
        survey.setTakenDate(LocalDate.now());

        // Tạo recommendation dựa trên từng loại khảo sát
        String recommendation = generateRecommendation(totalScore, surveyType);
        survey.setRecommendation(recommendation);

        surveyRepository.save(survey);
        surveyAnswerRepository.saveAll(survey.getAnswers());

        dashboardSurveyService.createIfNotExists(survey);
    }

    private Integer getOptionScore(Integer questionId, String answerText) {
        return surveyOptionRepository.findByQuestionQuestionIdAndValue(questionId, answerText)
                .map(SurveyOption::getScore)
                .orElse(null);
    }

    public String generateRecommendation(int totalScore, String surveyType) {
        if ("CRAFFT".equalsIgnoreCase(surveyType)) {
            if (totalScore <= 1) {
                return "Không có nguy cơ đáng kể. Tiếp tục duy trì lối sống lành mạnh bạn nhé.";
            } else if (totalScore == 2) {
                return "Nguy cơ trung bình. Nên theo dõi và tham khảo ý kiến chuyên gia nếu cần.";
            } else {
                return "Nguy cơ cao. Khuyến nghị tư vấn tâm lý hoặc chuyên gia ngay lập tức.";
            }
        } else {
            // Recommendation cho ASSIST (giữ nguyên logic cũ)
            if (totalScore == 0) return "Không có nguy cơ nào. Tiếp tục duy trì lối sống lành mạnh bạn nhé.";
            if (totalScore < 10) return "Nguy cơ thấp. Hãy duy trì lối sống lành mạnh.";
            if (totalScore < 20) return "Nguy cơ trung bình. Nên tham khảo chuyên gia.";
            return "Nguy cơ cao. Khuyến nghị tư vấn tâm lý ngay.";
        }
    }

    public List<Survey> getSurveysByUser(User user) {
        return surveyRepository.findByUser(user);
    }

    public Survey getSurveyById(Integer id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    public Optional<Survey> findSurveyById(Integer id) {
        return surveyRepository.findById(id);
    }

    public SurveyTemplate chooseTemplate(User user) {
        int age = LocalDate.now().getYear() - user.getYob();

        AgeGroup group;
        if (age >= 12 && age <= 18) {
            group = AgeGroup.ADOLESCENT;
        } else {
            group = AgeGroup.ADULTS;
        }

        List<SurveyTemplate> templates = surveyTemplateRepository.findByAgeGroup(group);
        if (templates.isEmpty()) throw new RuntimeException("No suitable survey found for this age group.");

        return templates.get(0);
    }

    public SurveyResponse toResponseDTO(Survey survey) {
        SurveyResponse dto = new SurveyResponse();
        dto.setSurveyId(survey.getSurveyId());
        dto.setSurveyType(survey.getSurveyType());
        dto.setStatus(survey.getStatus());
        dto.setTakenDate(survey.getTakenDate());
        dto.setTotalScore(survey.getTotalScore() != null ? survey.getTotalScore() : 0);
        dto.setRecommendation(survey.getRecommendation());
        dto.setUser(ProfileResponse.fromEntity(survey.getUser()));

        List<SurveyResponse.SurveyAnswerDTO> answerDTOs = survey.getAnswers().stream().map(answer -> {
            SurveyResponse.SurveyAnswerDTO a = new SurveyResponse.SurveyAnswerDTO();
            a.setQuestionId(answer.getQuestion().getQuestionId());
            a.setQuestionText(answer.getQuestion().getQuestionText());
            a.setAnswerText(answer.getAnswerText());
            a.setScore(answer.getScore());
            return a;
        }).toList();
        dto.setAnswers(answerDTOs);

        return dto;
    }

    public void deleteSurvey(Survey survey) {
        surveyRepository.delete(survey);
    }

    public List<Survey> getSurveysByUserAndTemplate(User user, SurveyTemplate template) {
        return surveyRepository.findByUserAndTemplate(user, template);
    }

    public SurveyRequestResponse toResponse(SurveyRequest request) {
        SurveyRequestResponse response = new SurveyRequestResponse();
        response.setName(request.getTemplate().getName());
        response.setId(request.getId());
        response.setReason(request.getReason());
        response.setRejectionReason(request.getRejectionReason());
        response.setRequestDate(request.getRequestDate());
        response.setStatus(request.getStatus());
        response.setUserId(request.getUser().getUserId());
        response.setTemplateId(request.getTemplate().getTemplateId());
        return response;
    }
}