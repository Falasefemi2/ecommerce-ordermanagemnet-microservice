package com.femi.paymentservice.controller;

import com.femi.paymentservice.model.Payment;
import com.femi.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment makePayment(@RequestParam Long orderId, @RequestParam double amount) {
        return paymentService.processPayment(orderId, amount);
    }
}
