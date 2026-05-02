package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems

fun OrderItems.toResponse(): OrderItemResponse {
    val product = this.product
        ?: throw IllegalStateException("Product not found for order item ${this.orderItemId}")
    return OrderItemResponse(
        orderItemId = this.orderItemId,
        productId = product.productId
            ?: throw IllegalStateException("Product ID not found for order item ${this.orderItemId}"),
        productName = this.productName,
        categoryName = product.category?.categoryName ?: "Unknown Category",
        productPrice = this.productPrice,
        productQuantity = this.productQuantity,
        productTotalPrice = productPrice * productQuantity,
        productImageUrl = this.productImageUrl,
        orderItemStatus = this.orderItemStatus,
        cancelReason = this.cancelReason
    )
}

fun OrderItems.toDetailResponse() = OrderDetailResponse(
    orderId = this.order.orderId,
    userName = this.order.userName,
    userEmail = this.order.userEmail,
    userPhone = this.order.userPhone,
    receiverName = this.order.receiverName,
    receiverPhone = this.order.receiverPhone,
    receiverZipcode = this.order.receiverZipcode,
    receiverAddr1 = this.order.receiverAddr1,
    receiverAddr2 = this.order.receiverAddr2,
    totalPrice = this.order.totalPrice,
    createdAt = this.order.createdAt,
    orderItem = this.toResponse()
)
