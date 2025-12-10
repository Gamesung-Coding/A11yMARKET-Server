package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderItemResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public record AdminOrderResponse(
        UUID orderId,
        String userName,
        String userEmail,
        String userPhone,
        String receiverName,
        String receiverPhone,
        String receiverZipcode,
        String receiverAddr1,
        String receiverAddr2,
        Integer totalPrice,
        List<OrderItemResponse> items,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime createdAt) {

    public static AdminOrderResponse fromEntity(Orders entity) {
        log.debug("AdminOrderResponse - fromEntity: Converting Orders entity to AdminOrderResponse DTO");
        var items = entity.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .toList();

        return new AdminOrderResponse(
                entity.getOrderId(),
                entity.getUserName(),
                entity.getUserEmail(),
                entity.getUserPhone(),
                entity.getReceiverName(),
                entity.getReceiverPhone(),
                entity.getReceiverZipcode(),
                entity.getReceiverAddr1(),
                entity.getReceiverAddr2(),
                entity.getTotalPrice(),
                items,
                entity.getCreatedAt()
        );
    }
}
