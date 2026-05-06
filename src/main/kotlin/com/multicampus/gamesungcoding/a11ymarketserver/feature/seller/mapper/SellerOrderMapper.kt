package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerOrderItemResponse

fun OrderItems.toSellerOrderItemResponse(): SellerOrderItemResponse {
    return SellerOrderItemResponse(
        orderItemId = this.orderItemId ?: throw DataNotFoundException("OrderItem ID is missing"),
        orderId = this.order.orderId ?: throw DataNotFoundException("Order ID is missing"),
        productId = this.product.productId ?: throw DataNotFoundException("Product ID is missing"),
        productName = this.productName,
        productPrice = this.productPrice,
        productQuantity = this.productQuantity,
        orderItemStatus = this.orderItemStatus,
        buyerName = this.order.userName,
        buyerEmail = this.order.userEmail,
        buyerPhone = this.order.userPhone,
        orderedAt = this.order.createdAt ?: throw DataNotFoundException("Order creation date not found")
    )
}

fun OrderItems.toOrderItemResponse(): OrderItemResponse {
    return OrderItemResponse(
        orderItemId = this.orderItemId ?: throw DataNotFoundException("OrderItem ID is missing"),
        productId = this.product.productId ?: throw DataNotFoundException("Product ID is missing"),
        productName = this.productName,
        categoryName = this.product.category?.categoryName
            ?: throw DataNotFoundException("Product category name is missing"),
        productPrice = this.productPrice,
        productQuantity = this.productQuantity,
        productTotalPrice = this.productQuantity * this.productPrice,
        productImageUrl = this.product.productImages[0].imageUrl
            ?: throw DataNotFoundException("Product image URL is missing"),
        orderItemStatus = this.orderItemStatus,
        cancelReason = this.cancelReason
    )
}
