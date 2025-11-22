package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    public List<AdminOrderResponse> getAllOrders() {
        return ordersRepository.findAll()
                .stream()
                .map(AdminOrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<OrderDetailResponse> getOrderDetails(UUID orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        List<OrderItems> items = orderItemsRepository.findByOrderId(orderId);

        return ResponseEntity.ok(OrderDetailResponse.fromEntity(order, items));
    }
}
