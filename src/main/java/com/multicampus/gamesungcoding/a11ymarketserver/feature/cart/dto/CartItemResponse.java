package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CartItemResponse(
        @NotBlank(message = "cart item id is required.")
        String cartItemId,

        @NotBlank(message = "cart id is required.")
        String cartId,

        @NotBlank(message = "product id is required.")
        String productId,

        @NotBlank(message = "quantity is required.")
        @Min(message = "quantity must be at least 1.", value = 1)
        Integer quantity) {
    
    public static CartItemResponse fromEntity(CartItems cartItems) {
        return new CartItemResponse(
                cartItems.getCartItemId().toString(),
                cartItems.getCartId().toString(),
                cartItems.getProductId().toString(),
                cartItems.getQuantity());
    }
}