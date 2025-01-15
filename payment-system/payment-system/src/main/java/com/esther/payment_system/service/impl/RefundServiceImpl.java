package com.esther.payment_system.service.impl;

import com.esther.payment_system.entity.Payment;
import com.esther.payment_system.entity.Refund;
import com.esther.payment_system.repository.PaymentRepository;
import com.esther.payment_system.repository.RefundRepository;
import com.esther.payment_system.service.contract.AccountService;
import com.esther.payment_system.service.contract.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final AccountService accountService;

    @Override
    public Refund processRefund(Long paymentId, Double amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new RuntimeException("Pagamento não encontrado"));

        if (amount > payment.getAmount()){
            throw new RuntimeException("Valor do reembolso excede o valor do pagamento");
        }

        Refund refund = Refund.builder()
                .paymentId(paymentId.toString())
                .amount(amount)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        //Atualizar saldo do cliente

        accountService.updateBalance(payment.getCustomerId(), amount);

        return refund;
    }
}
