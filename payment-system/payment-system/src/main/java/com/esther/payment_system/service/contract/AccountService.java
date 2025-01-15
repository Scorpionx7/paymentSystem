package com.esther.payment_system.service.contract;

public interface AccountService {

    void updateBalance(String customerId, Double amount);
    Double getBalance(String customerId);
}
