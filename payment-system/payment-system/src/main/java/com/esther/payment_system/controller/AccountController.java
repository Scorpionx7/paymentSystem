package com.esther.payment_system.controller;

import com.esther.payment_system.service.contract.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getBalance(customerId));
    }
}
