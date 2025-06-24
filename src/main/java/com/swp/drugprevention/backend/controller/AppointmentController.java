package com.swp.drugprevention.backend.controller;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.swp.drugprevention.backend.io.request.AppointmentRequest;
import com.swp.drugprevention.backend.io.response.AppointmentResponse;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService service;

    @PostMapping(value = "/create")
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = service.saveAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/getAllAppointment")
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
}
