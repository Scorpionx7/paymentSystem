package com.esther.payment_system.controller;

import com.esther.payment_system.entity.User;
import com.esther.payment_system.service.contract.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@AuthenticationPrincipal User user) {
        // O customerId é obtido diretamente do usuário autenticado.
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.ok(accountService.getBalance(customerId));
    }
}
