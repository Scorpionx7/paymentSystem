package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.enums.PaymentStatus;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.service.contract.NotificationService;
import com.esther.payment_system.service.contract.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    @Override
    public Payment createPayment(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.valueOf("PENDING")); // idealmente: PaymentStatus.PENDING.name()
        Payment saved = paymentRepository.save(payment);

        notificationService.sendNotification(
                saved.getCustomer().getId(),
                "Seu pagamento foi iniciado com sucesso."
        );

        return saved;
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        payment.setStatus(PaymentStatus.valueOf(status));
        payment.setUpdatedAt(LocalDateTime.now());

        Payment updated = paymentRepository.save(payment);

        notificationService.sendNotification(
                updated.getCustomer().getId(),
                "O status do seu pagamento foi atualizado para: " + status
        );

        return updated;
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomer_Id(customerId);
    }
}
