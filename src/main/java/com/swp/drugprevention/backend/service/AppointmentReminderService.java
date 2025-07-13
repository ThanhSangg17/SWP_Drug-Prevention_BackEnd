package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentReminderService {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000) // Chạy mỗi 1 phút (60.000 ms)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.plusHours(12);

        List<Appointment> upcoming = appointmentRepository.findByDateTimeBetween(
                target.minusMinutes(5), target.plusMinutes(5)
        );

        for (Appointment appointment : upcoming) {
            String email = appointment.getUser().getEmail();
            String name = appointment.getUser().getFullName();
            LocalDateTime time = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());

            emailService.sendAppointmentReminder(email, name, time);
        }
    }
}
