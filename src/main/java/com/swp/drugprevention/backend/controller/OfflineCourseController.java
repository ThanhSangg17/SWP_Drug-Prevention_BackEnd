package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.request.CourseActiveUpdateRequest;
import com.swp.drugprevention.backend.io.request.AttendanceUpdateRequest;
import com.swp.drugprevention.backend.io.request.SessionAttendanceRequest;
import com.swp.drugprevention.backend.io.response.OfflineCourseResponse;
import com.swp.drugprevention.backend.io.response.UserEnrolledCourseResponse;
import com.swp.drugprevention.backend.io.response.UserInfoResponse;
import com.swp.drugprevention.backend.io.response.CourseSessionResponse;
import com.swp.drugprevention.backend.model.*;
import com.swp.drugprevention.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/khoahoc")
public class OfflineCourseController {

    @Autowired
    private OfflineCourseRepository repository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseSessionRepository courseSessionRepository;

    @Autowired
    private SessionAttendanceRepository sessionAttendanceRepository;

    @GetMapping
    public List<OfflineCourse> getAllCourses() {
        return repository.findAll();
    }

    @PostMapping
    public OfflineCourse createCourse(@RequestBody OfflineCourse course) {
        Integer consultantId = course.getConsultant().getConsultantId();

        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant không tồn tại"));

        course.setConsultant(consultant);
        OfflineCourse saved = repository.save(course);

        for (int i = 0; i < 3; i++) {
            CourseSession session = new CourseSession();
            session.setCourse(saved);
            session.setSessionIndex(i + 1);
            session.setSessionDate(course.getStartTime().plusDays(i * 2));
            courseSessionRepository.save(session);
        }

        return saved;
    }

    @GetMapping("/{courseId}/sessions")
    public ResponseEntity<?> getSessionsByCourse(@PathVariable Long courseId,
                                                 @RequestParam Integer userId) {
        OfflineCourse course = repository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CourseSession> sessions = courseSessionRepository.findByCourse(course);

        List<CourseSessionResponse> responseList = sessions.stream().map(session -> {
            CourseSessionResponse dto = new CourseSessionResponse(
                    session.getId(),
                    session.getCourse().getId(),
                    session.getSessionIndex(),
                    session.getSessionDate()
            );
            sessionAttendanceRepository.findByUserAndSession(user, session)
                    .ifPresent(att -> dto.setIsPresent(att.getIsPresent()));
            return dto;
        }).toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/consultant/{consultantId}/sessions")
    public ResponseEntity<?> getSessionsByConsultant(@PathVariable Integer consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        List<OfflineCourse> courses = repository.findByConsultant(consultant);

        List<CourseSession> allSessions = courses.stream()
                .flatMap(course -> courseSessionRepository.findByCourse(course).stream())
                .toList();

        List<CourseSessionResponse> responseList = allSessions.stream().map(session -> new CourseSessionResponse(
                session.getId(),
                session.getCourse().getId(),
                session.getSessionIndex(),
                session.getSessionDate()
        )).toList();

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/session/diemdanh")
    public ResponseEntity<?> markSessionAttendance(@RequestBody SessionAttendanceRequest request) {
        CourseSession session = courseSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SessionAttendance attendance = sessionAttendanceRepository
                .findByUserAndSession(user, session)
                .orElse(new SessionAttendance());

        attendance.setUser(user);
        attendance.setSession(session);
        attendance.setIsPresent(request.getIsPresent());

        sessionAttendanceRepository.save(attendance);

        return ResponseEntity.ok("Attendance marked successfully.");
    }

    @GetMapping("/{id}")
    public OfflineCourse getCourse(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping("/dangky/{courseId}")
    public ResponseEntity<String> registerCourse(@PathVariable Long courseId,
                                                 @RequestParam Integer userId) {
        OfflineCourse course = repository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (enrollmentRepository.existsByUserAndOfflineCourse(user, course)) {
            return ResponseEntity.badRequest().body("User already registered for this course.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setOfflineCourse(course);
        enrollment.setEnrollDate(new Date(System.currentTimeMillis()));

        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok("Registered successfully.");
    }

    @GetMapping("/active")
    public ResponseEntity<List<OfflineCourseResponse>> getAllActiveCourses() {
        List<OfflineCourse> activeCourses = repository.findAll().stream()
                .filter(OfflineCourse::getActive)
                .toList();

        List<OfflineCourseResponse> dtoList = activeCourses.stream().map(course -> {
            OfflineCourseResponse dto = new OfflineCourseResponse();
            dto.setId(course.getId());
            dto.setCourseName(course.getCourseName());
            dto.setConsultant(course.getConsultant());
            dto.setPrice(course.getPrice());
            dto.setLocation(course.getLocation());
            dto.setStartTime(course.getStartTime());
            dto.setEndTime(course.getEndTime());
            dto.setMaxCapacity(course.getMaxCapacity());
            dto.setActive(course.getActive());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfflineCourseResponse>> getAllCoursesForAdmin() {
        List<OfflineCourse> allCourses = repository.findAll();

        List<OfflineCourseResponse> dtoList = allCourses.stream().map(course -> {
            OfflineCourseResponse dto = new OfflineCourseResponse();
            dto.setId(course.getId());
            dto.setCourseName(course.getCourseName());
            dto.setConsultant(course.getConsultant());
            dto.setPrice(course.getPrice());
            dto.setLocation(course.getLocation());
            dto.setStartTime(course.getStartTime());
            dto.setEndTime(course.getEndTime());
            dto.setMaxCapacity(course.getMaxCapacity());
            dto.setActive(course.getActive());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/danhsach-dangky/{consultantId}")
    public ResponseEntity<?> getRegisteredUsersByConsultant(@PathVariable Integer consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        List<OfflineCourse> courses = repository.findByConsultant(consultant);
        List<Enrollment> enrollments = enrollmentRepository.findByOfflineCourseIn(courses);

        List<UserInfoResponse> userInfoList = enrollments.stream()
                .map(e -> {
                    User user = e.getUser();
                    return new UserInfoResponse(
                            user.getUserId(),
                            user.getFullName(),
                            user.getEmail(),
                            e.getIsPresent() != null ? e.getIsPresent() : false,
                            e.getOfflineCourse().getId()
                    );
                }).toList();

        return ResponseEntity.ok(userInfoList);
    }

    @PutMapping("/diemdanh")
    public ResponseEntity<?> updateAttendance(@RequestBody AttendanceUpdateRequest request) {
        OfflineCourse course = repository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enrollment enrollment = enrollmentRepository.findByUserAndOfflineCourse(user, course)
                .orElseThrow(() -> new RuntimeException("User not registered for course"));

        enrollment.setIsPresent(request.getIsPresent());
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok("Attendance updated.");
    }

    @PutMapping("/diemdanh-hangloat")
    public ResponseEntity<?> updateAttendanceBatch(@RequestBody List<AttendanceUpdateRequest> requests) {
        for (AttendanceUpdateRequest req : requests) {
            OfflineCourse course = repository.findById(req.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found (id=" + req.getCourseId() + ")"));

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found (id=" + req.getUserId() + ")"));

            Enrollment enrollment = enrollmentRepository.findByUserAndOfflineCourse(user, course)
                    .orElseThrow(() -> new RuntimeException("User not registered (userId=" + req.getUserId() + ", courseId=" + req.getCourseId() + ")"));

            enrollment.setIsPresent(req.getIsPresent());
            enrollmentRepository.save(enrollment);
        }

        return ResponseEntity.ok("Batch attendance updated.");
    }

    @GetMapping("/khoahoc-cuatoi/{userId}")
    public ResponseEntity<?> getCoursesByUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        List<UserEnrolledCourseResponse> courseList = enrollments.stream()
                .map(e -> {
                    OfflineCourse course = e.getOfflineCourse();
                    return new UserEnrolledCourseResponse(
                            course.getId(),
                            course.getCourseName(),
                            course.getLocation(),
                            course.getStartTime(),
                            course.getEndTime(),
                            course.getConsultant()
                    );
                }).toList();

        return ResponseEntity.ok(courseList);
    }

    @PutMapping("/update-active")
    public ResponseEntity<?> updateCourseActive(@RequestBody CourseActiveUpdateRequest request) {
        OfflineCourse course = repository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setActive(request.getActive());
        repository.save(course);

        return ResponseEntity.ok("✅ Course active status updated.");
    }
}
