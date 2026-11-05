package com.hamza.support.order;

import java.time.LocalDate;

public record Order(
        String orderId,
        String customerId,
        Status status,
        LocalDate estimatedDelivery
) {
    public enum Status {
        PROCESSING,
        SHIPPED,
        DELIVERED
    }
}