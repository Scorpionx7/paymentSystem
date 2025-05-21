package com.esther.payment_system.service.contract;

import com.esther.payment_system.entity.Refund;

import java.math.BigDecimal;

public interface RefundService {

    Refund processRefund (Long paymentId, BigDecimal amount, String reason);
}
