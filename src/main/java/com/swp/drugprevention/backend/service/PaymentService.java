package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.enums.PaymentStatus;
import com.swp.drugprevention.backend.io.response.PaymentResponse;
import com.swp.drugprevention.backend.model.OfflineCourse;
import com.swp.drugprevention.backend.model.Payment;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.OfflineCourseRepository;
import com.swp.drugprevention.backend.repository.PaymentRepository;
import com.swp.drugprevention.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OfflineCourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VnPayService vnPayService;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public PaymentResponse createPaymentForCourse(Long courseId, Integer userId, String ipAddress) {
        OfflineCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = Payment.builder()
                .user(user)
                .course(course)
                .amount(course.getGiaTien())
                .paymentDate(new java.sql.Date(System.currentTimeMillis()))
                .paymentMethod("VNPAY")
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        String vnpUrl = vnPayService.createPaymentUrl(payment.getPaymentId().longValue(), (int) course.getGiaTien(), ipAddress);

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .qrCodeUrl(vnpUrl)
                .message("Tạo thanh toán thành công. Vui lòng quét mã hoặc nhấn vào link để thanh toán.") // ✅ thêm dòng này
                .build();
    }

    public Payment updatePaymentStatus(Integer paymentId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy payment ID: " + paymentId));

        payment.setStatus(newStatus);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Payment với ID: " + paymentId));
    }

}
