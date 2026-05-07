package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper.toResponse
import java.time.LocalDateTime
import java.util.*

data class AdminOrderResponse(
    val orderId: UUID,
    val userName: String,
    val userEmail: String,
    val userPhone: String?,
    val receiverName: String,
    val receiverPhone: String,
    val receiverZipcode: String,
    val receiverAddr1: String,
    val receiverAddr2: String?,
    val totalPrice: Int,
    val items: List<OrderItemResponse>,
    val createdAt: LocalDateTime?
)

fun Orders.toAdminResponse() = AdminOrderResponse(
    orderId = this.orderId!!,
    userName = this.userName,
    userEmail = this.userEmail,
    userPhone = this.userPhone,
    receiverName = this.receiverName,
    receiverPhone = this.receiverPhone,
    receiverZipcode = this.receiverZipcode,
    receiverAddr1 = this.receiverAddr1,
    receiverAddr2 = this.receiverAddr2,
    totalPrice = this.totalPrice,
    items = this.orderItems.map { it.toResponse() },
    createdAt = this.createdAt
)
