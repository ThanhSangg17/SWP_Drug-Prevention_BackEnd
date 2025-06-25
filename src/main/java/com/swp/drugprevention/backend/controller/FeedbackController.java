package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.FeedbackRequest;
import com.swp.drugprevention.backend.io.response.FeedBackConsultantResponse;
import com.swp.drugprevention.backend.io.response.FeedBackCourseResponse;
import com.swp.drugprevention.backend.io.response.FeedBackProgramResponse;
import com.swp.drugprevention.backend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService service;

    @PostMapping("/consultant/create")
    public ResponseEntity<?> createConsultantFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        try {
            FeedBackConsultantResponse response = service.saveConsultantFeedback(feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi tạo phản hồi cho consultant: " + e.getMessage()));
        }
    }

    @PostMapping("/course/create")
    public ResponseEntity<?> createCourseFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        try {
            FeedBackCourseResponse response = service.saveCourseFeedback(feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi tạo phản hồi cho course: " + e.getMessage()));
        }
    }

    @PostMapping("/program/create")
    public ResponseEntity<?> createProgramFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        try {
            FeedBackProgramResponse response = service.saveProgramFeedback(feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi tạo phản hồi cho program: " + e.getMessage()));
        }
    }

    @GetMapping("/consultant")
    public ResponseEntity<List<FeedBackConsultantResponse>> getAllConsultantFeedbacks() {
        try {
            List<FeedBackConsultantResponse> feedbacks = service.getAllConsultantFeedbacks();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/course")
    public ResponseEntity<List<FeedBackCourseResponse>> getAllCourseFeedbacks() {
        try {
            List<FeedBackCourseResponse> feedbacks = service.getAllCourseFeedbacks();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/program")
    public ResponseEntity<List<FeedBackProgramResponse>> getAllProgramFeedbacks() {
        try {
            List<FeedBackProgramResponse> feedbacks = service.getAllProgramFeedbacks();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/consultant/{feedbackId}")
    public ResponseEntity<?> getConsultantFeedbackById(@PathVariable("feedbackId") Integer id) {
        try {
            FeedBackConsultantResponse feedback = service.getConsultantFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi lấy thông tin phản hồi cho consultant: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{feedbackId}")
    public ResponseEntity<?> getCourseFeedbackById(@PathVariable("feedbackId") Integer id) {
        try {
            FeedBackCourseResponse feedback = service.getCourseFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi lấy thông tin phản hồi cho course: " + e.getMessage()));
        }
    }

    @GetMapping("/program/{feedbackId}")
    public ResponseEntity<?> getProgramFeedbackById(@PathVariable("feedbackId") Integer id) {
        try {
            FeedBackProgramResponse feedback = service.getProgramFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi lấy thông tin phản hồi cho program: " + e.getMessage()));
        }
    }

    @PutMapping("/consultant/update/{feedbackId}")
    public ResponseEntity<?> updateConsultantFeedback(@PathVariable("feedbackId") Integer id, @Valid @RequestBody FeedbackRequest request) {
        try {
            FeedBackConsultantResponse response = service.updateConsultantFeedback(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi cập nhật phản hồi cho consultant: " + e.getMessage()));
        }
    }

    @PutMapping("/course/update/{feedbackId}")
    public ResponseEntity<?> updateCourseFeedback(@PathVariable("feedbackId") Integer id, @Valid @RequestBody FeedbackRequest request) {
        try {
            FeedBackCourseResponse response = service.updateCourseFeedback(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi cập nhật phản hồi cho course: " + e.getMessage()));
        }
    }

    @PutMapping("/program/update/{feedbackId}")
    public ResponseEntity<?> updateProgramFeedback(@PathVariable("feedbackId") Integer id, @Valid @RequestBody FeedbackRequest request) {
        try {
            FeedBackProgramResponse response = service.updateProgramFeedback(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi cập nhật phản hồi cho program: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{feedbackId:\\d+}")
    public ResponseEntity<Map<String, String>> deleteFeedback(@PathVariable("feedbackId") Integer id) {
        try {
            service.deleteFeedback(id);
            return ResponseEntity.ok(Map.of("message", "Feedback deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting feedback: " + e.getMessage()));
        }
    }
}