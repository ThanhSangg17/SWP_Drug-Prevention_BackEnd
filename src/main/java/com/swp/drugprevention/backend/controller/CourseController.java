package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.Course;
import com.swp.drugprevention.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping("/getAllCourses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    @PostMapping("/createCourse")
    public Course createCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }
}
