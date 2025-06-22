package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.FeedbackRequest;
import com.swp.drugprevention.backend.model.Feedback;
import com.swp.drugprevention.backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService service;

    @PostMapping(value = "/create")
    Feedback create(@RequestBody FeedbackRequest feedbackRequest) {
        return service.saveFeedback(feedbackRequest);
    }

    @GetMapping(value = "/getAllFeedback")
    List<Feedback> getFeedback(){
        return service.getAllFeedbacks();
    }

    @GetMapping(value = "/getById/{feedbackId}")
    Feedback getFeedbackById(@PathVariable("feedbackId") Integer id){
        return service.getFeedbackById(id);
    }

    @PutMapping("/update/{feedbackId}")
    Feedback updateFeedback(@PathVariable("feedbackId") Integer id, @RequestBody FeedbackRequest request){
        return service.updateFeedback(id, request);
    }

    @DeleteMapping("/delete/{feedbackId}")
    String deleteFeedback(@PathVariable("feedbackId") Integer id){
        service.deleteFeedback(id);
        return "Feedback has been deleted";
    }
}