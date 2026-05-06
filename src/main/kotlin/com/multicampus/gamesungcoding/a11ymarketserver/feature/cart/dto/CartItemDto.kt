package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto

import java.util.*

data class CartItemDto(
    val cartItemId: UUID?,
    val cartId: UUID?,
    val productId: UUID,
    val sellerId: UUID,
    val sellerName: String,
    val productName: String,
    val productPrice: Int,
    val categoryName: String,
    val quantity: Int,
    val productImageUrl: String?
)