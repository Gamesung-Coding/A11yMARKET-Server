package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;

public record AdminOrderSearchRequest(
        String searchType,
        String keyword,
        OrderStatus status,
        String startDate,
        String endDate
) {
}
