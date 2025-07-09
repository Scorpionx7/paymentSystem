package com.esther.payment_system.service.contract;

import java.math.BigDecimal;

public interface AccountService {

    void credit(Long customerId, BigDecimal amount);
    void debit(Long customerId, BigDecimal amount);
    BigDecimal getBalance(Long customerId);
}
