package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import java.util.List;

public record AdminProductsResponse(Integer totalCount,
                                    List<ProductAdminInquireResponse> products) {
}
