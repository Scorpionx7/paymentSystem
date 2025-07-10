package com.esther.payment_system.service.impl;

import com.esther.payment_system.dto.PaymentRequest;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.enums.PaymentStatus;
import com.esther.payment_system.exception.InsufficientBalanceException;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.service.contract.AccountService;
import com.esther.payment_system.service.contract.NotificationService;
import com.esther.payment_system.service.contract.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final AccountService accountService;

    @Override
    @Transactional
    public Payment createPayment(PaymentRequest request, Customer customer) {
        // Inicializa o objeto de pagamento
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            // 1. TENTA DEBITAR O VALOR DA CONTA
            log.info("Tentando debitar {} da conta do cliente {}", request.getAmount(), customer.getId());
            accountService.debit(customer.getId(), request.getAmount());

            // 2. SE O DÉBITO FOR BEM-SUCEDIDO, APROVA O PAGAMENTO
            log.info("Débito bem-sucedido. Aprovando pagamento.");
            payment.setStatus(PaymentStatus.APPROVED);
            Payment saved = paymentRepository.save(payment);

            // 3. ENVIA NOTIFICAÇÃO DE SUCESSO
            notificationService.sendNotification(
                    saved.getCustomer().getId(),
                    saved.getId(),
                    "Seu pagamento de " + saved.getAmount() + " foi aprovado com sucesso."
            );
            return saved;

        } catch (InsufficientBalanceException e) {
            // 4. SE O SALDO FOR INSUFICIENTE, REJEITA O PAGAMENTO
            log.warn("Saldo insuficiente para o cliente {}. Rejeitando pagamento.", customer.getId());
            payment.setStatus(PaymentStatus.REJECTED);
            paymentRepository.save(payment);

            // 5. ENVIA NOTIFICAÇÃO DE FALHA
            notificationService.sendNotification(
                    payment.getCustomer().getId(),
                    payment.getId(),
                    "Seu pagamento de " + payment.getAmount() + " foi recusado por falta de saldo."
            );

            // Lança a exceção para que o controller possa tratá-la e informar o usuário
            throw e;
        }
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
                updated.getId(),
                "O status do seu pagamento foi atualizado para: " + status
        );

        return updated;
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomer_Id(customerId);
    }
}
