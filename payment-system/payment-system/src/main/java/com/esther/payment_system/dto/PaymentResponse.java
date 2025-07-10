package com.esther.payment_system.dto;

import com.esther.payment_system.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO final para a resposta da API de pagamentos
@Data
@Builder
public class PaymentResponse {
    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private CustomerResponse customer;
}