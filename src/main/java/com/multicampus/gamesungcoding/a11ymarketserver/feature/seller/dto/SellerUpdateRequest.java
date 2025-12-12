package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerUpdateRequest(@NotBlank String sellerName,
                                  @NotBlank String sellerIntro) {
}
