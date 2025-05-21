package com.esther.payment_system.service.impl;

import com.esther.payment_system.dto.NotificationMessage;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Notification;
import com.esther.payment_system.entity.enums.NotificationStatus;
import com.esther.payment_system.entity.enums.NotificationType;
import com.esther.payment_system.repository.CustomerRepository;
import com.esther.payment_system.repository.NotificationRepository;
import com.esther.payment_system.service.contract.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${exchange}")
    private String exchange;

    @Value("${routingKey}")
    private String routingKey;

    @Override
    public void sendNotification(Long customerId, String message) {
        NotificationMessage payload = new NotificationMessage(customerId, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        System.out.println("Notificação publicada na fila para o cliente: " + message);
    }
}
