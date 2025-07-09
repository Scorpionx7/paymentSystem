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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional // Garante que toda a operação seja atômica
    public Refund processRefund(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new IllegalArgumentException("Valor do reembolso excede o valor do pagamento");
        }

        // Verificação adicional: o pagamento não pode ser reembolsado se já foi reembolsado.
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Este pagamento já foi reembolsado.");
        }

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        // AQUI ESTÁ A CORREÇÃO:
        // No lugar de "PaymentStatus.REFUNDED.name();"
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        // O metodo updateBalance agora credita o valor na conta do cliente
        accountService.credit(payment.getCustomer().getId(), amount);

        // Notifica cliente
        notificationService.sendNotification(
                payment.getCustomer().getId(),
                "Seu pagamento no valor de " + amount + " foi reembolsado."
        );

        return refund;
    }
}

