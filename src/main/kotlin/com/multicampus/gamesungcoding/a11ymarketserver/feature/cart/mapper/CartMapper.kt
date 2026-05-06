package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemUpdatedResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems

fun CartItems.toDto(): CartItemDto {
    val cart = this.cart
    val product = this.product
    val seller = product?.seller
        ?: throw DataNotFoundException("Product with ID ${this.product?.productId} not found")

    val productImageUrl = if (product.productImages.isEmpty()) {
        null
    } else {
        product.productImages.first().imageUrl
    }

    return CartItemDto(
        this.cartItemId ?: throw DataNotFoundException("Cannot find itemId for CartItem"),
        cart.cartId,
        product.productId ?: throw DataNotFoundException("Cannot find related product for CartItem"),
        seller.sellerId ?: throw DataNotFoundException("Cannot find related seller for CartItem"),
        seller.sellerName,
        product.productName,
        product.productPrice,
        product.category?.categoryName ?: throw DataNotFoundException("Cannot find related category for CartItem"),
        this.quantity,
        productImageUrl
    )
}

fun CartItems.toUpdateResponse(): CartItemUpdatedResponse {
    return CartItemUpdatedResponse(
        this.cartItemId ?: throw DataNotFoundException("Cannot find itemId for CartItem"),
        this.cart.cartId ?: throw DataNotFoundException("Cannot find cartId for CartItem"),
        this.product?.productId ?: throw DataNotFoundException("Cannot find related product for CartItem"),
        this.quantity
    )
}