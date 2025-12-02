package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import jakarta.validation.constraints.NotNull;

public record SellerOrderItemsStatusUpdateRequest(
        @NotNull OrderItemStatus status) {
}