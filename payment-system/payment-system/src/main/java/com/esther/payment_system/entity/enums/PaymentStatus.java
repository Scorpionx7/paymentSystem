package com.esther.payment_system.entity.enums;

public enum PaymentStatus {
    PENDING,     // Pagamento iniciado, aguardando confirmação
    APPROVED,    // Pagamento aprovado
    REJECTED,    // Pagamento recusado pela instituição
    CANCELLED,   // Pagamento cancelado manualmente
    REFUNDED,    // Pagamento reembolsado
    FAILED       // Falha no processamento
}
