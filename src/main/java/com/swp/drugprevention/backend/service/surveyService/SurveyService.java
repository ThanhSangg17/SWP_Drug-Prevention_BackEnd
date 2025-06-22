package com.swp.drugprevention.backend.service.surveyService;

import com.swp.drugprevention.backend.enums.AgeGroup;
import com.swp.drugprevention.backend.io.request.StartSurveyRequest;
import com.swp.drugprevention.backend.io.request.SubmitSurveyRequest;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import com.swp.drugprevention.backend.io.response.SurveyResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.*;
import com.swp.drugprevention.backend.repository.CourseRepository;
import com.swp.drugprevention.backend.repository.ProgramRepository;
import com.swp.drugprevention.backend.repository.surveyRepo.*;
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

    // Bắt đầu một survey mới từ template phù hợp với tuổi
    public Survey startSurvey(User user) {
        SurveyTemplate template = chooseTemplate(user);
        if (template == null) {
            throw new IllegalStateException("No survey template available for this user.");
        }

        Survey survey = Survey.builder()
                .user(user)
                .template(template)
                .surveyType(template.getSurveyType().name())
                .takenDate(LocalDate.now())
                .status("In Progress")
                .program(null)
                .course(null)
                .totalScore(0) // Khởi tạo totalScore mặc định là 0
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


    // Nộp survey sau khi người dùng hoàn thành
    public void submitSurvey(Survey survey, SubmitSurveyRequest request) {
        Map<Integer, SubmitSurveyRequest.AnswerDTO> answerMap = new HashMap<>();
        for (SubmitSurveyRequest.AnswerDTO dto : request.getAnswers()) {
            answerMap.put(dto.getQuestionId(), dto);
        }

        int totalScore = 0;

        for (SurveyAnswer answer : survey.getAnswers()) {
            SubmitSurveyRequest.AnswerDTO dto = answerMap.get(answer.getQuestion().getQuestionId());
            if (dto != null) {
                answer.setAnswerText(dto.getAnswerText());
                Integer questionScore = getOptionScore(answer.getQuestion().getQuestionId(), dto.getAnswerText());
                if (questionScore != null) {
                    answer.setScore(questionScore);
                    totalScore += questionScore;
                } else {
                    // Nếu không tìm thấy score, có thể để mặc định là 0
                    answer.setScore(0);
                    System.out.println("Warning: No score found for questionId: " + answer.getQuestion().getQuestionId() +
                            ", answerText: " + dto.getAnswerText());
                }

            }
        }
//không tính đc điểm một phần là do value nhập vào không khớp với giá trị trong survey_options -> tiếng anh tiếng việt
        //hãy cập nhật lại value trong file json
        survey.setStatus("Completed");
        survey.setTotalScore(totalScore);
        survey.setTakenDate(LocalDate.now());

        //format recommendation based on total score
        String recommendation = generateRecommendation(totalScore);
        survey.setRecommendation(recommendation);

        surveyRepository.save(survey);
        surveyAnswerRepository.saveAll(survey.getAnswers());

        dashboardSurveyService.createIfNotExists(survey);
    }

    // Phương thức mới để lấy score từ survey_options
    private Integer getOptionScore(Integer questionId, String answerText) {
        return surveyOptionRepository.findByQuestionQuestionIdAndValue(questionId, answerText)
                .map(SurveyOption::getScore)
                .orElse(null); // Trả về null nếu không tìm thấy
    }

    // Sinh khuyến nghị dựa vào tổng điểm
    public String generateRecommendation(int totalScore) {
        if (totalScore == 0) return "Không có nguy cơ nào. Tiếp tục duy trì lối sống lành mạnh bạn nhé.";
        if (totalScore < 10) return "Nguy cơ thấp. Hãy duy trì lối sống lành mạnh.";
        if (totalScore < 20) return "Nguy cơ trung bình. Nên tham khảo chuyên gia.";
        return "Nguy cơ cao. Khuyến nghị tư vấn tâm lý ngay.";
    }

    // Lấy danh sách survey theo user
    public List<Survey> getSurveysByUser(User user) {
        return surveyRepository.findByUser(user);
    }

    // Lấy chi tiết survey theo ID
    public Survey getSurveyById(Integer id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    public Optional<Survey> findSurveyById(Integer id) {
        return surveyRepository.findById(id);
    }

    // Chọn template phù hợp theo độ tuổi user
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
        dto.setTotalScore(survey.getTotalScore() != null ? survey.getTotalScore() : 0); // Gán 0 nếu null
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

}
