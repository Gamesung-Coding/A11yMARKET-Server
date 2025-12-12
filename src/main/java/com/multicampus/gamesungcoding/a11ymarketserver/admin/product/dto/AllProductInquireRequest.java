package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;

public record AllProductInquireRequest(String query,
                                       ProductStatus status,
                                       Integer page,
                                       Integer size) {
}
