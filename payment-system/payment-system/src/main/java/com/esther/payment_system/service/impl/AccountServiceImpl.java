package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Account;
import com.esther.payment_system.repository.AccountRepository;
import com.esther.payment_system.service.contract.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public void updateBalance(String customerId, Double amount) {
        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(()-> new RuntimeException("Conta não encontrada para o cliente:" + customerId));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public Double getBalance(String customerId) {
        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(()-> new RuntimeException("Conta não encontrada para o cliente: "+ customerId));

        return account.getBalance();
    }
}
