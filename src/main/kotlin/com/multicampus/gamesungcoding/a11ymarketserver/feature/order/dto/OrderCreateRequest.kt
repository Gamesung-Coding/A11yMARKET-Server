package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

data class OrderCreateRequest(
    val addressId: String,
    val cartItemIds: List<String>? = null,
    val directOrderItem: OrderRequestItem? = null
)
