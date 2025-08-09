package com.femi.orderservice.service;

import com.femi.orderservice.client.PaymentClient;
import com.femi.orderservice.client.PaymentResponse;
import com.femi.orderservice.client.ProductClient;
import com.femi.orderservice.client.ProductResponse;
import com.femi.orderservice.model.Order;
import com.femi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public Order placeOrder(Long productId, int quantity) {
        ProductResponse product = productClient.getProductById(productId);

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
        productClient.updateProduct(productId, product);

        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .totalPrice(product.getPrice() * quantity)
                .orderDate(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);
        log.info("Order saved with ID: {}", order.getId());

        try {
            log.info("Calling payment service for order: {} with amount: {}",
                    order.getId(), order.getTotalPrice());

            PaymentResponse payment = paymentClient.makePayment(order.getId(), order.getTotalPrice());

            log.info("Payment response received: {}", payment);
            log.info("Payment status: {}", payment.getStatus());

            if (!"SUCCESS".equals(payment.getStatus())) {
                log.error("Payment failed. Expected 'SUCCESS', but got: '{}'", payment.getStatus());
                throw new RuntimeException("Payment failed for order: " + order.getId());
            }

            log.info("Payment successful for order: {}", order.getId());
            return order;

        } catch (Exception e) {
            log.error("Error during payment processing: ", e);
            throw e;
        }
    }
}
