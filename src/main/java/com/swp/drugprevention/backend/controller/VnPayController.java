package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.VnPayTransaction;
import com.swp.drugprevention.backend.repository.VnPayTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnPayController {

    private final VnPayTransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public ResponseEntity<List<VnPayTransaction>> getTransactions() {
        List<VnPayTransaction> list = transactionRepository.findAllByOrderByPayDateDesc();
        return ResponseEntity.ok(list);
    }
}
