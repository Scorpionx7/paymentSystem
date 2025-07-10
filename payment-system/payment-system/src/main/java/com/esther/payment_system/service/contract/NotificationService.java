package com.esther.payment_system.service.contract;

public interface NotificationService {

    void sendNotification(Long customerId, Long paymentId, String message);

}
