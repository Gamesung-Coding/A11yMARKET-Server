package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders

fun Orders.toResponse() = OrderResponse(
    orderId = this.orderId,
    totalPrice = this.totalPrice,
    orderItems = this.orderItems.map { it.toResponse() },
    createdAt = this.createdAt
)