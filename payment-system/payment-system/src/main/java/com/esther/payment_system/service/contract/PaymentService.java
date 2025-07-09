package com.esther.payment_system.service.contract;

import com.esther.payment_system.dto.PaymentRequest;
import com.esther.payment_system.entity.Customer;
import com.esther.payment_system.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment(PaymentRequest request, Customer customer);
    Payment updatePaymentStatus(Long paymentId, String status);
    List<Payment> getPaymentsByCustomerId(Long customerId);

}
