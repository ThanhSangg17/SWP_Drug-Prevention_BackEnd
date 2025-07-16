package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.enums.ConsultationStatus;
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

    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.plusHours(12);

        List<Appointment> upcoming = appointmentRepository.findByStatus(ConsultationStatus.Pending);

        for (Appointment appointment : upcoming) {
            if (Boolean.TRUE.equals(appointment.getReminderSent())) {
                continue;
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(
                    appointment.getDate(), appointment.getStartTime());

            LocalDateTime reminderWindowStart = appointmentDateTime.minusHours(12);
            LocalDateTime reminderWindowEnd = reminderWindowStart.plusMinutes(5);

            if (now.isAfter(reminderWindowStart) && now.isBefore(reminderWindowEnd)) {
                String email = appointment.getUser().getEmail();
                String name = appointment.getUser().getFullName();

                emailService.sendAppointmentReminder(email, name, appointmentDateTime);

                appointment.setReminderSent(true);
                appointmentRepository.save(appointment);
            }
        }
    }
}