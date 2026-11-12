package com.reloop.transactionservice.controller;

import com.reloop.transactionservice.dto.request.CreateOrderRequest;
import com.reloop.transactionservice.dto.response.OrderResponse;
import com.reloop.transactionservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "test-buyer") String customerId) {

        OrderResponse response = orderService.createOrder(request, customerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            @RequestHeader(value = "X-User-Id", defaultValue = "test-buyer") String customerId,
            Pageable pageable) {

        return ResponseEntity.ok(orderService.getMyOrders(customerId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "test-buyer") String customerId) {

        return ResponseEntity.ok(orderService.getOrderById(id, customerId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "test-buyer") String customerId) {

        return ResponseEntity.ok(orderService.cancelOrder(id, customerId));
    }
}