package com.swp.drugprevention.backend.repository.surveyRepo;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.survey.SurveyRequest;
import com.swp.drugprevention.backend.model.survey.SurveyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRequestRepository extends JpaRepository<SurveyRequest, Long> {
    List<SurveyRequest> findByStatus(String status);
    Optional<SurveyRequest> findByUserAndTemplateAndStatus(User user, SurveyTemplate template, String status);
    // Tùy chọn: Lọc theo user và status
    List<SurveyRequest> findByUserAndStatus(User user, String status);
    List<SurveyRequest> findByUser(User user);
}
