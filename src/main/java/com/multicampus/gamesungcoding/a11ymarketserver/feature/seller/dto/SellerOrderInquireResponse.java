package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import java.util.List;

public record SellerOrderInquireResponse(List<SellerOrderItemResponse> orderItems,
                                         Integer totalOrderCount) {
}
