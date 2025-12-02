package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import java.util.List;

public enum OrderItemStatus {
    ORDERED,
    PAID,
    REJECTED,
    ACCEPTED,
    SHIPPED,
    CONFIRMED,
    CANCEL_PENDING,
    CANCELED,
    CANCEL_REJECTED,
    RETURN_PENDING,
    RETURNED,
    RETURN_REJECTED;

    public static List<OrderItemStatus> inProgressStatuses() {
        return List.of(ORDERED, PAID, ACCEPTED, SHIPPED, CANCEL_PENDING, RETURN_PENDING);
    }
}
