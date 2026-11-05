package com.hamza.support.order;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse getOrder(
            String orderId,
            Authentication authentication
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(this::orderUnavailable);

        boolean canReadAny = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("orders:read:any")
                );

        boolean canReadOwn = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("orders:read:own")
                );

        boolean isOwner = order.customerId()
                .equals(authentication.getName());

        if (!canReadAny && !(canReadOwn && isOwner)) {
            throw orderUnavailable();
        }

        return OrderResponse.from(order);
    }

    private ResponseStatusException orderUnavailable() {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Order not found or unavailable to your account"
        );
    }
}