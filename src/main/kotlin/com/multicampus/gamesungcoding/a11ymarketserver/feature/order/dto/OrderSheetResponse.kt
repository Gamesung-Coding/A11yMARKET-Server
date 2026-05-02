package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto

data class OrderSheetResponse(
    val items: List<CartItemDto>,
    val totalAmount: Int,
    val shippingFee: Int,
    val finalAmount: Int
)
