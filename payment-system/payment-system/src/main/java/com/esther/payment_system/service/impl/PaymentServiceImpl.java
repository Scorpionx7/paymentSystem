package com.esther.payment_system.service.impl;

import com.esther.payment_system.dto.PaymentRequest;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.enums.PaymentStatus;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.service.contract.AccountService;
import com.esther.payment_system.service.contract.NotificationService;
import com.esther.payment_system.service.contract.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final AccountService accountService;

    @Override
    public Payment createPayment(PaymentRequest request, Customer customer) { // Assinatura alterada
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING) // Usando o Enum diretamente
                .customer(customer) // Associando ao cliente autenticado
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        notificationService.sendNotification(
                saved.getCustomer().getId(),
                "Seu pagamento foi iniciado com sucesso."
        );

        return saved;
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());

        // Lógica de débito
        if (newStatus == PaymentStatus.APPROVED && payment.getStatus() != PaymentStatus.APPROVED) {
            accountService.debit(payment.getCustomer().getId(), payment.getAmount());
        }

        payment.setStatus(newStatus);
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
