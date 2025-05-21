package com.esther.payment_system.service.contract;

import java.math.BigDecimal;

public interface AccountService {
    void updateBalance(Long customerId, BigDecimal amount);
    BigDecimal getBalance(Long customerId);
}
