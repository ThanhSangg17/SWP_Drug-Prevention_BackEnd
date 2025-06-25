package com.swp.drugprevention.backend.repository.surveyRepo;

import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.Survey;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    List<Survey> findByUser(User user);

    // Thêm phương thức để lấy bản ghi gần nhất (tùy chọn thay vì List)
    @Query("SELECT s FROM Survey s WHERE s.user = :user AND s.template = :template ORDER BY s.takenDate DESC, s.surveyId DESC")
    Optional<Survey> findFirstByUserAndTemplateOrderByTakenDateDesc(User user, SurveyTemplate template);

    List<Survey> findByUserAndTemplate(User user, SurveyTemplate template);

    List<Survey> findByUserAndTemplateAndStatusNot(User user, SurveyTemplate template, String status);
    long countByUserAndTemplateAndStatusNot(User user, SurveyTemplate template, String status);
}
