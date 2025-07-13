package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.response.OfflineCourseResponse;
import com.swp.drugprevention.backend.io.response.UserInfoResponse;
import com.swp.drugprevention.backend.model.OfflineCourse;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.model.Enrollment;
import com.swp.drugprevention.backend.repository.OfflineCourseRepository;
import com.swp.drugprevention.backend.repository.ConsultantRepository;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.repository.EnrollmentRepository;
import com.swp.drugprevention.backend.io.response.OfflineCourseResponse;
import com.swp.drugprevention.backend.io.request.AttendanceUpdateRequest;
import com.swp.drugprevention.backend.io.response.UserEnrolledCourseResponse;
import com.swp.drugprevention.backend.model.CourseSession;
import com.swp.drugprevention.backend.repository.CourseSessionRepository;
import com.swp.drugprevention.backend.io.response.CourseSessionResponse;
import com.swp.drugprevention.backend.io.request.SessionAttendanceRequest;
import com.swp.drugprevention.backend.repository.SessionAttendanceRepository;
import com.swp.drugprevention.backend.model.SessionAttendance;

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

        // 🆕 Tạo 3 buổi học cách nhau 2 ngày
        for (int i = 0; i < 3; i++) {
            CourseSession session = new CourseSession();
            session.setCourse(saved);
            session.setSessionIndex(i + 1);
            session.setSessionDate(course.getThoiGianBatDau().plusDays(i * 2));
            courseSessionRepository.save(session);
        }

        return saved;
    }


    @GetMapping("/{courseId}/sessions")
    public ResponseEntity<?> getSessionsByCourse(@PathVariable Long courseId,
                                                 @RequestParam Integer userId) {
        OfflineCourse course = repository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        List<CourseSession> sessions = courseSessionRepository.findByCourse(course);

        List<CourseSessionResponse> responseList = sessions.stream().map(session -> {
            CourseSessionResponse dto = new CourseSessionResponse(
                    session.getId(),
                    session.getCourse().getId(),
                    session.getSessionIndex(),
                    session.getSessionDate()
            );

            // ✅ Gán điểm danh nếu tồn tại
            sessionAttendanceRepository.findByUserAndSession(user, session)
                    .ifPresent(att -> dto.setIsPresent(att.getIsPresent()));

            return dto;
        }).toList();

        return ResponseEntity.ok(responseList);
    }


    @GetMapping("/consultant/{consultantId}/sessions")
    public ResponseEntity<?> getSessionsByConsultant(@PathVariable Integer consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant không tồn tại"));

        // Lấy tất cả khóa học của consultant
        List<OfflineCourse> courses = repository.findByConsultant(consultant);

        // Lấy tất cả buổi học thuộc các khóa đó
        List<CourseSession> allSessions = courses.stream()
                .flatMap(course -> courseSessionRepository.findByCourse(course).stream())
                .toList();

        // Convert sang DTO
        List<CourseSessionResponse> responseList = allSessions.stream().map(session -> new CourseSessionResponse(
                session.getId(),
                session.getCourse().getId(),
                session.getSessionIndex(),
                session.getSessionDate()
        )).toList();

        return ResponseEntity.ok(responseList);
    }




    @Autowired
    private SessionAttendanceRepository sessionAttendanceRepository;


    @PutMapping("/session/diemdanh")
    public ResponseEntity<?> markSessionAttendance(@RequestBody SessionAttendanceRequest request) {
        CourseSession session = courseSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Buổi học không tồn tại"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        SessionAttendance attendance = sessionAttendanceRepository
                .findByUserAndSession(user, session)
                .orElse(new SessionAttendance());

        attendance.setUser(user);
        attendance.setSession(session);
        attendance.setIsPresent(request.getIsPresent());

        sessionAttendanceRepository.save(attendance);

        return ResponseEntity.ok("Điểm danh buổi học thành công");
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
    public ResponseEntity<String> registerCourse(@PathVariable  Long courseId, @RequestParam Integer userId) {
        OfflineCourse course = repository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (enrollmentRepository.existsByUserAndOfflineCourse(user, course)) {
            return ResponseEntity.badRequest().body("User đã đăng ký khóa học này.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setOfflineCourse(course);
        enrollment.setEnrollDate(new Date(System.currentTimeMillis()));

        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok("Đăng ký khóa học thành công.");
    }
    @GetMapping("/getallcourse")
    public ResponseEntity<List<OfflineCourseResponse>> getAllCourseWithInfo() {
        List<OfflineCourse> courseList = repository.findAll();

        List<OfflineCourseResponse> dtoList = courseList.stream().map(course -> {
            OfflineCourseResponse dto = new OfflineCourseResponse();
            dto.setId(course.getId());
            dto.setTenKhoaHoc(course.getTenKhoaHoc());
            dto.setConsultant(course.getConsultant());
            dto.setGiaTien(course.getGiaTien());
            dto.setDiaDiem(course.getDiaDiem());
            dto.setThoiGianBatDau(course.getThoiGianBatDau());
            dto.setThoiGianKetThuc(course.getThoiGianKetThuc());
            dto.setSoLuongToiDa(course.getSoLuongToiDa());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/danhsach-dangky/{consultantId}")
    public ResponseEntity<?> getRegisteredUsersByConsultant(@PathVariable Integer consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant không tồn tại"));

        List<OfflineCourse> courses = repository.findByConsultant(consultant);

        List<Enrollment> enrollments = enrollmentRepository.findByOfflineCourseIn(courses);

        List<UserInfoResponse> userInfoList = enrollments.stream()
                .map(e -> {
                    User user = e.getUser();
                    return new UserInfoResponse(
                            user.getUserId(),
                            user.getFullName(),
                            user.getEmail(),
                            e.getIsPresent() != null ? e.getIsPresent() : false, // tránh null
                            e.getOfflineCourse().getId() // ✅ Thêm courseId tại đây

                    );
                })
                .toList(); // không cần distinct nếu vẫn cần nhiều lần điểm danh

        return ResponseEntity.ok(userInfoList);
    }

    @PutMapping("/diemdanh")
    public ResponseEntity<?> updateAttendance(@RequestBody AttendanceUpdateRequest request) {
        OfflineCourse course = repository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Enrollment enrollment = enrollmentRepository.findByUserAndOfflineCourse(user, course)
                .orElseThrow(() -> new RuntimeException("User chưa đăng ký khóa học này"));

        enrollment.setIsPresent(request.getIsPresent());
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok("Điểm danh thành công.");
    }

    @PutMapping("/diemdanh-hangloat")
    public ResponseEntity<?> updateAttendanceBatch(@RequestBody List<AttendanceUpdateRequest> requests) {
        for (AttendanceUpdateRequest req : requests) {
            OfflineCourse course = repository.findById(req.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại (id=" + req.getCourseId() + ")"));

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại (id=" + req.getUserId() + ")"));

            Enrollment enrollment = enrollmentRepository.findByUserAndOfflineCourse(user, course)
                    .orElseThrow(() -> new RuntimeException("User chưa đăng ký khóa học (userId=" + req.getUserId() + ", courseId=" + req.getCourseId() + ")"));

            enrollment.setIsPresent(req.getIsPresent());
            enrollmentRepository.save(enrollment);
        }

        return ResponseEntity.ok("Cập nhật điểm danh hàng loạt thành công.");
    }

    @GetMapping("/khoahoc-cuatoi/{userId}")
    public ResponseEntity<?> getCoursesByUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        List<UserEnrolledCourseResponse> courseList = enrollments.stream()
                .map(e -> {
                    OfflineCourse course = e.getOfflineCourse();
                    return new UserEnrolledCourseResponse(
                            course.getId(),
                            course.getTenKhoaHoc(),
                            course.getDiaDiem(),
                            course.getThoiGianBatDau(),
                            course.getThoiGianKetThuc(),
                            course.getConsultant()
                    );
                })
                .toList();

        return ResponseEntity.ok(courseList);
    }






}
