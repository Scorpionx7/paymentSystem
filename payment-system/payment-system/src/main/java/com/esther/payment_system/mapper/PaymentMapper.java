package com.esther.payment_system.mapper;

import com.esther.payment_system.dto.CustomerResponse;
import com.esther.payment_system.dto.PaymentResponse;
import com.esther.payment_system.dto.UserResponse;
import com.esther.payment_system.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        UserResponse userResponse = UserResponse.builder()
                .id(payment.getCustomer().getUser().getId())
                .username(payment.getCustomer().getUser().getUsername())
                .build();

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(payment.getCustomer().getId())
                .name(payment.getCustomer().getName())
                .phoneNumber(payment.getCustomer().getPhoneNumber())
                .user(userResponse)
                .build();

        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .customer(customerResponse)
                .build();
    }
}
