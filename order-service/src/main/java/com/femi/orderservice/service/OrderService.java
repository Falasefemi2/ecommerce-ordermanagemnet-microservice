package com.femi.orderservice.service;

import com.femi.orderservice.client.PaymentClient;
import com.femi.orderservice.client.PaymentResponse;
import com.femi.orderservice.client.ProductClient;
import com.femi.orderservice.client.ProductResponse;
import com.femi.orderservice.model.Order;
import com.femi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
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

        PaymentResponse payment = paymentClient.makePayment(order.getId(), order.getTotalPrice());

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new RuntimeException("Payment failed for order: " + order.getId());
        }

        return order;
    }
}
