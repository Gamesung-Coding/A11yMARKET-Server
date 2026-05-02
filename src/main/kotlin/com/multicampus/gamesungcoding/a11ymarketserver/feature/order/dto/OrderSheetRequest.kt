package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

data class OrderSheetRequest(
    val cartItemIds: List<String>? = null,
    val directOrderItem: OrderRequestItem? = null
)
