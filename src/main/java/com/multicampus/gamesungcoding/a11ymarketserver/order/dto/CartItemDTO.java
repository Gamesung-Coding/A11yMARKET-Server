package com.multicampus.gamesungcoding.a11ymarketserver.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private UUID productID;
    private String productName;
    private long price; //현재 가격
    private int quantity;
    private long subtotal; //price * quantity
}
