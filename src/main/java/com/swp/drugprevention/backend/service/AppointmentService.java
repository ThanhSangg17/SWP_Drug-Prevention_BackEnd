package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.AppointmentRequest;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment saveAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setTime(request.getTime());
        appointment.setDate(request.getDate());
        appointment.setLocation(request.getLocation());
        appointment.setStatus(request.getStatus());
        return appointmentRepository.save(appointment);
    }
    public Appointment getAppointmentById(Integer id){
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    public Appointment updateAppointment(Integer id,AppointmentRequest request){
        Appointment appointment = getAppointmentById(id);
        appointment.setTime(request.getTime());
        appointment.setDate(request.getDate());
        appointment.setLocation(request.getLocation());
        appointment.setStatus(request.getStatus());
        return appointmentRepository.save(appointment);
    }
    public void deleteAppointment(Integer id){
        appointmentRepository.deleteById(id);
    }
}
