package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.AppointmentRequest;
import com.swp.drugprevention.backend.io.response.AppointmentResponse;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.AppointmentRepository;
import com.swp.drugprevention.backend.repository.ConsultantRepository;
import com.swp.drugprevention.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ConsultantRepository consultantRepository;

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment -> new AppointmentResponse(
                        appointment.getAppointmentId(),
                        appointment.getDate(),
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        appointment.getStatus(),
                        appointment.getLocation(),
                        appointment.getUser() != null ? appointment.getUser().getUserId() : null,
                        appointment.getConsultant() != null ? appointment.getConsultant().getConsultantId() : null
                ))
                .collect(Collectors.toList());
    }
    public AppointmentResponse saveAppointment(@Valid AppointmentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        LocalDate date = request.getDate();
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusHours(2);

        Consultant consultant = consultantRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found with ID: " + request.getConsultantId()));

        // 🔎 Kiểm tra xem có appointment nào trùng hoặc cách dưới 30 phút
        List<Appointment> existingAppointments = appointmentRepository.findByConsultantAndDate(consultant, date);
        for (Appointment appt : existingAppointments) {
            LocalTime existingStart = appt.getStartTime();
            LocalTime existingEnd = appt.getEndTime();

            // Nếu thời gian mới nằm trong khoảng [start-30, end+30]
            if (!(endTime.plusMinutes(30).isBefore(existingStart) || startTime.minusMinutes(30).isAfter(existingEnd))) {
                throw new IllegalStateException("Đã có cuộc hẹn gần thời gian này. Vui lòng chọn khung giờ khác cách ít nhất 30 phút.");
            }
        }

        // ✅ Tạo appointment
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(request.getStatus());
        appointment.setLocation(request.getLocation());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));
        appointment.setUser(user);
        appointment.setConsultant(consultant);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new AppointmentResponse(
                savedAppointment.getAppointmentId(),
                savedAppointment.getDate(),
                savedAppointment.getStartTime(),
                savedAppointment.getEndTime(),
                savedAppointment.getStatus(),
                savedAppointment.getLocation(),
                request.getUserId(),
                request.getConsultantId()
        );
    }


    public Appointment getAppointmentById (Integer id){
            return appointmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
        }
        public AppointmentResponse updateAppointment (Integer id, AppointmentRequest request){
            Appointment appointment = getAppointmentById(id);
            appointment.setStartTime(request.getStartTime());
            appointment.setEndTime(request.getEndTime());
            appointment.setDate(request.getDate());
            appointment.setLocation(request.getLocation());
            appointment.setStatus(request.getStatus());
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));
            appointment.setUser(user);

            Consultant consultant = consultantRepository.findById(request.getConsultantId())
                    .orElseThrow(() -> new RuntimeException("Consultant not found with ID: " + request.getConsultantId()));
            appointment.setConsultant(consultant);

            Appointment savedAppointment = appointmentRepository.save(appointment);
            return new AppointmentResponse(
                    savedAppointment.getAppointmentId(),
                    savedAppointment.getDate(),
                    savedAppointment.getStartTime(),
                    savedAppointment.getEndTime(),
                    savedAppointment.getStatus(),
                    savedAppointment.getLocation(),
                    request.getUserId(),
                    request.getConsultantId()
            );
        }

        public void deleteAppointment (Integer id){
            appointmentRepository.deleteById(id);
        }

    public AppointmentResponse getMyAppointment(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        LocalDate today = LocalDate.now();

        List<Appointment> appointments = appointmentRepository
                .findByUserAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(user, today);


        Appointment appointment = appointments.getFirst();
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus(),
                appointment.getLocation(),
                user.getUserId(),
                appointment.getConsultant() != null ? appointment.getConsultant().getConsultantId() : null
        );
    }

}
