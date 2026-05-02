package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import java.util.*

data class OrderItemResponse(
    val orderItemId: UUID,
    val productId: UUID,
    val productName: String,
    val categoryName: String,
    val productPrice: Int,
    val productQuantity: Int,
    val productTotalPrice: Int,
    val productImageUrl: String?,
    val orderItemStatus: OrderItemStatus,
    val cancelReason: String?
)
