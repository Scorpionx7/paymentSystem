package com.esther.payment_system.service.contract;

import com.esther.payment_system.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment (Payment payment);
    Payment updatePaymentStatus(Long paymentId, String status);
    List<Payment> getPaymentsByCustomerId(Long customerId);

}
