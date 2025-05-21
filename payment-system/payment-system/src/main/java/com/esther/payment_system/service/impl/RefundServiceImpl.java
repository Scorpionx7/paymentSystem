package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.Refund;
import com.esther.payment_system.entity.enums.PaymentStatus;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.repository.RefundRepository;
import com.esther.payment_system.service.contract.AccountService;
import com.esther.payment_system.service.contract.NotificationService;
import com.esther.payment_system.service.contract.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final AccountService accountService;
    private final NotificationService notificationService;

    @Override
    public Refund processRefund(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new IllegalArgumentException("Valor do reembolso excede o valor do pagamento");
        }

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        accountService.updateBalance(payment.getCustomer().getId(), amount);

        // Atualiza status do pagamento
        PaymentStatus.REFUNDED.name(); // ou PaymentStatus.REFUNDED.name()
        paymentRepository.save(payment);

        // Notifica cliente
        notificationService.sendNotification(
                payment.getCustomer().getId(),
                "Seu pagamento foi reembolsado com sucesso."
        );

        return refund;
    }
}

