package com.swp.drugprevention.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendResetOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Your otp for resetting your password is "+otp+". Use this OTP to proceed with resetting your password.");
        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Account Verification OTP");
        message.setText("Your OTP is "+otp+". Verify your account using this OTP");
        mailSender.send(message);
    }
    public void sendAppointmentReminder(String to, String userName, LocalDateTime appointmentTime) {
        String subject = "Nhắc lịch hẹn tư vấn";
        String body = String.format("""
                Mến chào %s,
                
                Đây là email nhắc nhở bạn về cuộc hẹn sắp tới vào lúc %s.
                
                Vui lòng chuẩn bị và đến đúng giờ. Nếu bạn không thể tham gia, vui lòng hủy trước thời gian hẹn.

                Trân trọng,
                Hệ thống dự án tư vấn phòng chống ma túy !!!.
                """, userName, appointmentTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }
}
