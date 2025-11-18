package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
public record CartItemListResponse(
        List<CartItemResponse> items,

        @Positive(message = "Total must be positive")
        int total) {
}