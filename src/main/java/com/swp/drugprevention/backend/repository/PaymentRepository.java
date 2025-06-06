package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
