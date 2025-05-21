package com.esther.payment_system.consumer;

import com.esther.payment_system.dto.NotificationMessage;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Notification;
import com.esther.payment_system.entity.enums.NotificationStatus;
import com.esther.payment_system.entity.enums.NotificationType;
import com.esther.payment_system.repository.CustomerRepository;
import com.esther.payment_system.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "${queue}")
    public void receiveNotification(NotificationMessage message) {
        Customer customer = customerRepository.findById(message.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Notification notification = Notification.builder()
                .customer(customer)
                .message(message.getMessage())
                .sentAt(LocalDateTime.now())
                .status(NotificationStatus.SENT)
                .type(NotificationType.EMAIL)
                .build();

        notificationRepository.save(notification);
        System.out.println("Notificação recebida da fila e salva para cliente: " + customer.getEmail());
    }
}
