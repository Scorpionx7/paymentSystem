package com.esther.payment_system.controller;

import com.esther.payment_system.entity.Refund;
import com.esther.payment_system.service.contract.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<Refund> processRefund(
            @RequestParam Long paymentId,
            @RequestParam BigDecimal amount,
            @RequestParam String reason
    ) {
        Refund refund = refundService.processRefund(paymentId, amount, reason);
        return ResponseEntity.ok(refund);
    }
}
