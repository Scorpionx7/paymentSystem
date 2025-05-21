package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Account;
import com.esther.payment_system.repository.AccountRepository;
import com.esther.payment_system.service.contract.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.esther.payment_system.exception.AccountNotFoundException;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public void updateBalance(Long customerId, BigDecimal amount) {
        Account account = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o cliente: " + customerId));

        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar essa operação.");
        }

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(Long customerId) {
        return accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o cliente: " + customerId))
                .getBalance();
    }
}


