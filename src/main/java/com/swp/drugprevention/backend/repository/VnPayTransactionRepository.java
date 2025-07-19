package com.swp.drugprevention.backend.repository;


import com.swp.drugprevention.backend.model.VnPayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VnPayTransactionRepository extends JpaRepository<VnPayTransaction, String> {
    List<VnPayTransaction> findAllByOrderByPayDateDesc();
}

