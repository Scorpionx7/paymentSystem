package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.service.contract.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationServiceImpl notificationService;

    @Override
    public Payment createPayment(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus("PENDING");
        Payment savedPayment = paymentRepository.save(payment);

        // enviar notificação de criação
        notificationService.sendNotification(
                savedPayment.getCustomerId(),
                "Seu pagamento foi iniciado com sucesso"
        );

     return savedPayment;
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        payment.setStatus(status);
        payment.setUpdateAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(String customerId) {
        return paymentRepository.findAll()
                .stream()
                .filter(payment -> payment.getCustomerId().equals(customerId))
                .toList();
    }

}
