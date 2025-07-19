package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.enums.PaymentStatus;
import com.swp.drugprevention.backend.io.response.PaymentResponse;
import com.swp.drugprevention.backend.model.Payment;
import com.swp.drugprevention.backend.model.VnPayTransaction;
import com.swp.drugprevention.backend.repository.VnPayTransactionRepository;
import com.swp.drugprevention.backend.service.PaymentService;
import com.swp.drugprevention.backend.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final VnPayService vnPayService;

    @PostMapping("/course/{courseId}/user/{userId}")
    public ResponseEntity<PaymentResponse> createCoursePayment(
            @PathVariable Long courseId,
            @PathVariable Integer userId,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        return ResponseEntity.ok(paymentService.createPaymentForCourse(courseId, userId, ip));
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> handleVnPayReturn(@RequestParam Map<String, String> allParams) {
        String result = vnPayService.handleReturn(allParams);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam PaymentStatus status) {

        Payment updated = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok("✅ Cập nhật trạng thái thành công: " + updated.getStatus());
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }


}
