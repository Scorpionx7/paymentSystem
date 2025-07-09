package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Account;
import com.esther.payment_system.exception.InsufficientBalanceException;
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
    public void credit(Long customerId, BigDecimal amount) {
        Account account = getAccountByCustomerId(customerId);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Override
    public void debit(Long customerId, BigDecimal amount) {
        Account account = getAccountByCustomerId(customerId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar essa operação.");
        }
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(Long customerId) {
        return getAccountByCustomerId(customerId).getBalance();
    }

    // Metodo auxiliar para evitar repetição de código
    private Account getAccountByCustomerId(Long customerId) {
        return accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o cliente: " + customerId));
    }
}


