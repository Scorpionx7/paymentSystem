package com.esther.payment_system.service.contract;

import com.esther.payment_system.entity.Refund;

public interface RefundService {

    Refund processRefund (Long paymentId, Double amount, String reason);
}
