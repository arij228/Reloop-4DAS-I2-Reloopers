package com.reloop.transactionservice.service;

import com.reloop.transactionservice.dto.request.CreateOrderItemRequest;
import com.reloop.transactionservice.dto.request.CreateOrderRequest;
import com.reloop.transactionservice.dto.response.OrderResponse;
import com.reloop.transactionservice.entity.CustomerOrder;
import com.reloop.transactionservice.entity.OrderItem;
import com.reloop.transactionservice.enums.OrderStatus;
import com.reloop.transactionservice.exception.BusinessRuleException;
import com.reloop.transactionservice.exception.ResourceNotFoundException;
import com.reloop.transactionservice.mapper.OrderMapper;
import com.reloop.transactionservice.repository.CustomerOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final BigDecimal DELIVERY_FEE = new BigDecimal("7.000"); // Flat 7 TND

    public OrderService(CustomerOrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String customerId) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(customerId);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setListingId(itemReq.getListingId());
            item.setSellerId(itemReq.getSellerId());
            item.setMaterialName(itemReq.getMaterialName());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());

            // lineTotal = quantity * unitPrice
            BigDecimal lineTotal = itemReq.getQuantity().multiply(itemReq.getUnitPrice());
            item.setLineTotal(lineTotal);

            subtotal = subtotal.add(lineTotal);
            order.addItem(item);
        }

        order.setSubtotal(subtotal);
        order.setDeliveryFee(DELIVERY_FEE);
        // total = subtotal + delivery - discount
        order.setTotalAmount(subtotal.add(DELIVERY_FEE).subtract(order.getDiscountAmount()));

        CustomerOrder savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(String customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id, String customerId) {
        CustomerOrder order = orderRepository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found or access denied"));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id, String customerId) {
        CustomerOrder order = orderRepository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found or access denied"));

        // Business Rule: Can only cancel if not paid yet
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessRuleException("Cannot cancel order. Current status is: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        CustomerOrder updatedOrder = orderRepository.save(order);
        return orderMapper.toResponse(updatedOrder);
    }

    private String generateOrderNumber() {
        // Format: ORD-2026-123456 (Using UUID for simplicity and guarantee of uniqueness in v1)
        String year = String.valueOf(LocalDateTime.now().getYear());
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + year + "-" + randomPart;
    }
}