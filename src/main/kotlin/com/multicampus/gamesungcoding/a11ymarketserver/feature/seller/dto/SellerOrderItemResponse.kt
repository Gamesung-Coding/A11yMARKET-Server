package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import java.time.LocalDateTime
import java.util.*

data class SellerOrderItemResponse(
    val orderItemId: UUID,
    val orderId: UUID,
    val productId: UUID,
    val productName: String,
    val productPrice: Int,
    val productQuantity: Int,
    val orderItemStatus: OrderItemStatus,
    val buyerName: String,
    val buyerEmail: String,
    val buyerPhone: String?,
    val orderedAt: LocalDateTime
)
