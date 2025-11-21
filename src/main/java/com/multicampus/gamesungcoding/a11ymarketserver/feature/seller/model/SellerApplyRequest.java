package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.NotBlank;

public record SellerApplyRequest(
        @NotBlank String sellerName,
        @NotBlank String businessNumber,
        String sellerIntro) {
}