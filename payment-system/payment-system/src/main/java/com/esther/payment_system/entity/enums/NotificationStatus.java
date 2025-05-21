package com.esther.payment_system.entity.enums;

public enum NotificationStatus {

    PENDING,    // Aguardando envio
    SENT,       // Enviada com sucesso
    FAILED,     // Falhou ao enviar
    RETRYING    // Tentando reenviar
}
