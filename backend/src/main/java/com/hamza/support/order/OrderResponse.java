package com.hamza.support.order;

import java.time.LocalDate;

public record OrderResponse(
        String orderId,
        Order.Status status,
        LocalDate estimatedDelivery
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.orderId(),
                order.status(),
                order.estimatedDelivery()
        );
    }
}