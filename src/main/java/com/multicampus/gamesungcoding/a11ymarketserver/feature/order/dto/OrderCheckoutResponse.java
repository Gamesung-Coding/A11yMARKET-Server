package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCheckoutResponse {
    private Long totalAmount; // 총 상품 금액
    private Long shippingFee; // 배송비 (지금은 0)
    private Long finalAmount; // totalAmount + shippingFee


}
