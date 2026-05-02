package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import java.time.LocalDateTime
import java.util.*

data class OrderResponse(
    val orderId: UUID,
    val totalPrice: Int,
    val orderItems: List<OrderItemResponse>,
    val createdAt: LocalDateTime
)
