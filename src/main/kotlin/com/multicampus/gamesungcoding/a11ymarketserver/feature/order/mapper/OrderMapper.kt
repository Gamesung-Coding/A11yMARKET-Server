package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders

fun Orders.toResponse() = OrderResponse(
    orderId = this.orderId ?: throw InvalidRequestException("Order ID is not found"),
    totalPrice = this.totalPrice,
    orderItems = this.orderItems.map { it.toResponse() },
    createdAt = this.createdAt ?: throw InvalidRequestException("Order creation date is not found")
)