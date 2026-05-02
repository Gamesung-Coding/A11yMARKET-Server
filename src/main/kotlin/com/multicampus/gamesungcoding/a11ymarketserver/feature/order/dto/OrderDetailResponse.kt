package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import java.time.LocalDateTime
import java.util.*

data class OrderDetailResponse(
    val orderId: UUID,
    val userName: String,
    val userEmail: String,
    val userPhone: String,
    val receiverName: String,
    val receiverPhone: String,
    val receiverZipcode: String,
    val receiverAddr1: String,
    val receiverAddr2: String?,
    val totalPrice: Int,
    val createdAt: LocalDateTime,
    val orderItem: OrderItemResponse
)
