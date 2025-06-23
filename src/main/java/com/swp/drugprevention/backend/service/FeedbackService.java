package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.FeedbackRequest;
import com.swp.drugprevention.backend.model.Feedback;
import com.swp.drugprevention.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

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
        feedbackRepository.deleteById(id);
    }
}