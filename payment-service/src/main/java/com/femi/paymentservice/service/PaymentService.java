package com.femi.paymentservice.service;

import com.femi.paymentservice.model.Payment;
import com.femi.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    public Payment processPayment(Long orderId, double amount){
        boolean success = random.nextBoolean();

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(success ? "Success" : "Failed")
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }
}
