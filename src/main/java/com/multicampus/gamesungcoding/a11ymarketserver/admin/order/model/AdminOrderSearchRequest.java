package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

public record AdminOrderSearchRequest(
        String searchType,
        String keyword,
        String startDate,
        String endDate
) {
}
