package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentVerifyResponse(
        UUID orderId,
        String status,
        Integer amount,
        LocalDateTime paidAt
) {
    public static PaymentVerifyResponse success(UUID orderId, Integer amount) {
        return new PaymentVerifyResponse(
                orderId,
                "PAID",
                amount,
                LocalDateTime.now()
        );
    }
}
