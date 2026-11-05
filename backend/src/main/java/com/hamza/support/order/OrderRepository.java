package com.hamza.support.order;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderRepository {

    private final Map<String, Order> orders = Map.of(
            "ORD-1001", new Order(
                    "ORD-1001",
                    "customer-a",
                    Order.Status.SHIPPED,
                    LocalDate.of(2026, 11, 7)
            ),
            "ORD-1002", new Order(
                    "ORD-1002",
                    "customer-b",
                    Order.Status.DELIVERED,
                    LocalDate.of(2026, 11, 3)
            ),
            "ORD-1003", new Order(
                    "ORD-1003",
                    "customer-a",
                    Order.Status.PROCESSING,
                    LocalDate.of(2026, 11, 10)
            )
    );

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
}