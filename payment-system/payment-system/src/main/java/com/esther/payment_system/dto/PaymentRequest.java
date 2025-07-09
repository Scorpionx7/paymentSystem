package com.esther.payment_system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "O valor não pode ser nulo")
    @DecimalMin(value = "0.01", message = "O valor do pagamento deve ser maior que zero")
    private BigDecimal amount;
}
