package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Notification;
import com.esther.payment_system.repository.NotificationRepository;
import com.esther.payment_system.service.contract.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String customerId, String message) {
        Notification notification = Notification.builder()
                .customerId(customerId)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        System.out.println("Notificação enviada para o cliente: " + message);
    }
}
