package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.dtoRequest.AppointmentRequest;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService service;

    @PostMapping(value = "/create")
    Appointment create(@RequestBody AppointmentRequest appointmentRequest) {
        return service.saveAppointment(appointmentRequest);
    }
    @GetMapping(value = "/getAllAppointment")
    List<Appointment> getAppointment(){
        return service.getAllAppointments();
    }
    @GetMapping(value = "/getById/{appointmentId}")
    Appointment getAppointment(@PathVariable("appointmentId") Integer id){
        return service.getAppointmentById(id);
    }
    @PutMapping("/update/{appointmentId}")
    Appointment updateAppointment(@PathVariable("appointmentId") Integer id,@RequestBody AppointmentRequest request){
        return service.updateAppointment(id, request);
    }
    @DeleteMapping("/delete/{appointmentId}")
    String deleteAppointment(@PathVariable("appointmentId") Integer id){
        service.deleteAppointment(id);
        return "Appointment has been deleted";
    }
}
