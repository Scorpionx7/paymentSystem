package com.esther.payment_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private UserResponse user;
}