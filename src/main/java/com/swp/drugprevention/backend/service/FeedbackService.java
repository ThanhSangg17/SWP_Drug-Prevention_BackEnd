package com.swp.drugprevention.backend.service;

<<<<<<< ThanhSang1
import com.swp.drugprevention.backend.io.FeedbackRequest;
import com.swp.drugprevention.backend.io.response.FeedBackConsultantResponse;
import com.swp.drugprevention.backend.io.response.FeedBackCourseResponse;
import com.swp.drugprevention.backend.io.response.FeedBackProgramResponse;
=======
import com.swp.drugprevention.backend.io.request.FeedbackRequest;
>>>>>>> main
import com.swp.drugprevention.backend.model.Feedback;
import com.swp.drugprevention.backend.repository.ConsultantRepository;
import com.swp.drugprevention.backend.repository.FeedbackRepository;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.CourseRepository;
import com.swp.drugprevention.backend.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProgramRepository programRepository;

    public List<FeedBackConsultantResponse> getAllConsultantFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAllByConsultantIsNotNull();
        return feedbacks.stream()
                .map(feedback -> new FeedBackConsultantResponse(
                        feedback.getFeedbackId(),
                        feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                        feedback.getConsultant() != null ? feedback.getConsultant().getConsultantId() : null,
                        feedback.getDate(),
                        feedback.getContent()
                ))
                .collect(Collectors.toList());
    }

    public List<FeedBackCourseResponse> getAllCourseFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAllByCourseIsNotNull();
        return feedbacks.stream()
                .map(feedback -> new FeedBackCourseResponse(
                        feedback.getFeedbackId(),
                        feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                        feedback.getCourse() != null ? feedback.getCourse().getCourseId() : null,
                        feedback.getDate(),
                        feedback.getContent()
                ))
                .collect(Collectors.toList());
    }

<<<<<<< ThanhSang1
    public List<FeedBackProgramResponse> getAllProgramFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAllByProgramIsNotNull();
        return feedbacks.stream()
                .map(feedback -> new FeedBackProgramResponse(
                        feedback.getFeedbackId(),
                        feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                        feedback.getProgram() != null ? feedback.getProgram().getProgramId() : null,
                        feedback.getDate(),
                        feedback.getContent()
                ))
                .collect(Collectors.toList());
    }

    public FeedBackConsultantResponse saveConsultantFeedback(FeedbackRequest request) {
        if (request == null || request.getContent() == null) {
            throw new IllegalArgumentException("Request or content cannot be null");
        }
        if (request.getConsultantId() == null) {
            throw new IllegalArgumentException("Consultant ID cannot be null for consultant feedback");
        }
        Feedback feedback = new Feedback();
        feedback.setContent(request.getContent());
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        feedback.setConsultant(consultantRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found with ID: " + request.getConsultantId())));
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return new FeedBackConsultantResponse(
                savedFeedback.getFeedbackId(),
                savedFeedback.getUser() != null ? savedFeedback.getUser().getUserId() : null,
                savedFeedback.getConsultant() != null ? savedFeedback.getConsultant().getConsultantId() : null,
                savedFeedback.getDate(),
                savedFeedback.getContent()
        );
    }

    public FeedBackCourseResponse saveCourseFeedback(FeedbackRequest request) {
        if (request == null || request.getContent() == null) {
            throw new IllegalArgumentException("Request or content cannot be null");
        }
        if (request.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID cannot be null for course feedback");
        }
        Feedback feedback = new Feedback();
        feedback.setContent(request.getContent());
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        feedback.setCourse(courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId())));
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return new FeedBackCourseResponse(
                savedFeedback.getFeedbackId(),
                savedFeedback.getUser() != null ? savedFeedback.getUser().getUserId() : null,
                savedFeedback.getCourse() != null ? savedFeedback.getCourse().getCourseId() : null,
                savedFeedback.getDate(),
                savedFeedback.getContent()
        );
    }

    public FeedBackProgramResponse saveProgramFeedback(FeedbackRequest request) {
        if (request == null || request.getContent() == null) {
            throw new IllegalArgumentException("Request or content cannot be null");
        }
        if (request.getProgramId() == null) {
            throw new IllegalArgumentException("Program ID cannot be null for program feedback");
        }
        Feedback feedback = new Feedback();
        feedback.setContent(request.getContent());
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        feedback.setProgram(programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found with ID: " + request.getProgramId())));
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return new FeedBackProgramResponse(
                savedFeedback.getFeedbackId(),
                savedFeedback.getUser() != null ? savedFeedback.getUser().getUserId() : null,
                savedFeedback.getProgram() != null ? savedFeedback.getProgram().getProgramId() : null,
                savedFeedback.getDate(),
                savedFeedback.getContent()
        );
    }

    public FeedBackConsultantResponse updateConsultantFeedback(Integer id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        if (request.getContent() != null) {
            feedback.setContent(request.getContent());
        }
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        if (request.getConsultantId() != null) {
            feedback.setConsultant(consultantRepository.findById(request.getConsultantId())
                    .orElseThrow(() -> new RuntimeException("Consultant not found with ID: " + request.getConsultantId())));
        }
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return new FeedBackConsultantResponse(
                updatedFeedback.getFeedbackId(),
                updatedFeedback.getUser() != null ? updatedFeedback.getUser().getUserId() : null,
                updatedFeedback.getConsultant() != null ? updatedFeedback.getConsultant().getConsultantId() : null,
                updatedFeedback.getDate(),
                updatedFeedback.getContent()
        );
    }

    public FeedBackCourseResponse updateCourseFeedback(Integer id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        if (request.getContent() != null) {
            feedback.setContent(request.getContent());
        }
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        if (request.getCourseId() != null) {
            feedback.setCourse(courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId())));
        }
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return new FeedBackCourseResponse(
                updatedFeedback.getFeedbackId(),
                updatedFeedback.getUser() != null ? updatedFeedback.getUser().getUserId() : null,
                updatedFeedback.getCourse() != null ? updatedFeedback.getCourse().getCourseId() : null,
                updatedFeedback.getDate(),
                updatedFeedback.getContent()
        );
    }

    public FeedBackProgramResponse updateProgramFeedback(Integer id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        if (request.getContent() != null) {
            feedback.setContent(request.getContent());
        }
        if (request.getUserId() != null) {
            feedback.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId())));
        }
        if (request.getProgramId() != null) {
            feedback.setProgram(programRepository.findById(request.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found with ID: " + request.getProgramId())));
        }
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return new FeedBackProgramResponse(
                updatedFeedback.getFeedbackId(),
                updatedFeedback.getUser() != null ? updatedFeedback.getUser().getUserId() : null,
                updatedFeedback.getProgram() != null ? updatedFeedback.getProgram().getProgramId() : null,
                updatedFeedback.getDate(),
                updatedFeedback.getContent()
        );
    }

    public FeedBackConsultantResponse getConsultantFeedbackById(Integer id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        return new FeedBackConsultantResponse(
                feedback.getFeedbackId(),
                feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                feedback.getConsultant() != null ? feedback.getConsultant().getConsultantId() : null,
                feedback.getDate(),
                feedback.getContent()
        );
    }

    public FeedBackCourseResponse getCourseFeedbackById(Integer id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        return new FeedBackCourseResponse(
                feedback.getFeedbackId(),
                feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                feedback.getCourse() != null ? feedback.getCourse().getCourseId() : null,
                feedback.getDate(),
                feedback.getContent()
        );
    }

    public FeedBackProgramResponse getProgramFeedbackById(Integer id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
        return new FeedBackProgramResponse(
                feedback.getFeedbackId(),
                feedback.getUser() != null ? feedback.getUser().getUserId() : null,
                feedback.getProgram() != null ? feedback.getProgram().getProgramId() : null,
                feedback.getDate(),
                feedback.getContent()
        );
    }

    public void deleteFeedback(Integer id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
=======
    public Feedback saveFeedback(FeedbackRequest request) {
        Feedback feed = new Feedback();
        feed.setContent(request.getContent());
        return feedbackRepository.save(feed);
    }

    public Feedback getFeedbackById(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public Feedback updateFeedback(Integer id, FeedbackRequest request) {
        Feedback feed = getFeedbackById(id);
        feed.setContent(request.getContent());
        return feedbackRepository.save(feed);
    }

    public void deleteFeedback(Integer id) {
>>>>>>> main
        feedbackRepository.deleteById(id);
    }
}