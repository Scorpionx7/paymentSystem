package com.esther.payment_system.controller;


import com.esther.payment_system.dto.PaymentRequest;
import com.esther.payment_system.dto.PaymentResponse;
import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.User;
import com.esther.payment_system.exception.InsufficientBalanceException;
import com.esther.payment_system.mapper.PaymentMapper;
import com.esther.payment_system.service.contract.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest request, @AuthenticationPrincipal User user) {
        try {
            Payment paymentEntity = paymentService.createPayment(request, user.getCustomer());
            PaymentResponse paymentResponse = paymentMapper.toPaymentResponse(paymentEntity);
            return ResponseEntity.ok(paymentResponse);
        } catch (InsufficientBalanceException e) {
            // Retorna um erro 400 Bad Request com uma mensagem clara
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Payment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Payment updated = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getMyPayments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(user.getCustomer().getId()));
    }


}
