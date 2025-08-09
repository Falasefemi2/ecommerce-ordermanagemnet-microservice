package com.femi.orderservice.client;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private double amount;
    private String status;
    private LocalDateTime paymentDate;
}