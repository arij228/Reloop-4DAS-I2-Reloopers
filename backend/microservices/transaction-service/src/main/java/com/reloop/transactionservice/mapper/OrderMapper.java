package com.reloop.transactionservice.mapper;

import com.reloop.transactionservice.dto.response.OrderItemResponse;
import com.reloop.transactionservice.dto.response.OrderResponse;
import com.reloop.transactionservice.entity.CustomerOrder;
import com.reloop.transactionservice.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(CustomerOrder order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().name());
        response.setSubtotal(order.getSubtotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setTotalAmount(order.getTotalAmount());
        response.setCurrency(order.getCurrency());
        response.setCreatedAt(order.getCreatedAt());

        response.setItems(order.getItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList()));

        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setListingId(item.getListingId());
        dto.setMaterialName(item.getMaterialName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setLineTotal(item.getLineTotal());
        return dto;
    }
}