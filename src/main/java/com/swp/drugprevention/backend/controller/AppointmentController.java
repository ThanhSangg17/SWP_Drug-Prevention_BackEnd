package com.swp.drugprevention.backend.controller;
import com.swp.drugprevention.backend.enums.ConsultationStatus;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.swp.drugprevention.backend.io.request.AppointmentRequest;
import com.swp.drugprevention.backend.io.response.AppointmentResponse;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService service;

    @PutMapping("/consultant/update-status/{appointmentId}")
//    @PreAuthorize("hasRole('CONSULTANT')")
    public ResponseEntity<?> updateStatus(@PathVariable Integer appointmentId,
                                          @RequestParam ConsultationStatus status) {
        AppointmentResponse response = service.setStatus(appointmentId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consultant/{consultantId}")
//    @PreAuthorize("hasRole('STAFF') or hasRole('CONSULTANT')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByConsultant(@PathVariable Integer consultantId) {
        List<AppointmentResponse> list = service.getAppointmentsByConsultant(consultantId);
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = service.saveAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-slot-availability")
    // Check slot available nếu consultant ở thời gian đó, ngày đó đã có lịch hẹn thì sẽ trả về false, ngược lại sẽ trả về true
    // Mục đích cho fe để hiển thị nút đặt lịch hẹn với consultant, nếu consultant không có lịch hẹn thì sẽ hiển thị consultant đó
    // lên và sẽ chọn được, còn nếu consultant đã có lịch hẹn thì sẽ không hiển thị consultant đó lên
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Integer consultantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime) {
        boolean isAvailable = service.isSlotAvailable(consultantId, date, startTime);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping(value = "/getAllAppointment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('MANAGER')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> list = service.getAllAppointments();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value = "/getById/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointmentId") Integer id) {
        Appointment appointment = service.getAppointmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(appointment);
    }

    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@Valid @PathVariable("appointmentId") Integer id, @RequestBody AppointmentRequest request) {
        AppointmentResponse response = service.updateAppointment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("Đã cập nhật thành công !!!");
    }

    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@Valid @PathVariable("appointmentId") Integer id) {
        service.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Đã xóa thành công !!!");
    }

    @GetMapping("/myAppointment")
    public ResponseEntity<?> getMyAppointments(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        List<AppointmentResponse> appointments = service.getMyAppointments(email);

        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Bạn chưa có lịch hẹn nào"));
        }

        return ResponseEntity.ok(appointments);
    }

    //Hủy lịch hẹn dành cho staff
    @PutMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasRole('STAFF')")
    // Hủy lịch hẹn, chỉ cần truyền vào appointmentId, sẽ cập nhật status thành CANCELLED
    public ResponseEntity<?> cancelAppointment(@PathVariable("appointmentId") Integer appointmentId) {
        AppointmentResponse response = service.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Đã hủy lịch hẹn thành công !!!");
    }



}
