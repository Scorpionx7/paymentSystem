package com.esther.payment_system.consumer;

import com.esther.payment_system.dto.NotificationMessage;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Notification;
import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.enums.NotificationStatus;
import com.esther.payment_system.entity.enums.NotificationType;
import com.esther.payment_system.repository.CustomerRepository;
import com.esther.payment_system.repository.NotificationRepository;
import com.esther.payment_system.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "${queue}")
    @Transactional
    public void receiveNotification(NotificationMessage message) {
        Customer customer = customerRepository.findById(message.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para notificação: " + message.getCustomerId()));

        Payment payment = paymentRepository.findById(message.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado para notificação: " + message.getPaymentId()));

        Notification notification = Notification.builder()
                .customer(customer)
                .payment(payment)
                .message(message.getMessage())
                .sentAt(LocalDateTime.now())
                .status(NotificationStatus.SENT)
                .type(NotificationType.EMAIL)
                .build();

        notificationRepository.save(notification);
        System.out.println("Notificação recebida da fila e salva para cliente: " + customer.getEmail());
    }
}
