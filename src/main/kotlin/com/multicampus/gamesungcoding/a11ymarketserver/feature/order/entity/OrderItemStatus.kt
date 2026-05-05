package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity


enum class OrderItemStatus {
    ORDERED,
    PAID,
    REJECTED,
    ACCEPTED,
    SHIPPING,
    SHIPPED,
    CONFIRMED,
    CANCEL_PENDING,
    CANCELED,
    CANCEL_REJECTED,
    RETURN_PENDING,
    RETURNED,
    RETURN_REJECTED;

    companion object {
        val inProgressStatuses: List<OrderItemStatus>
            get() = listOf(ORDERED, PAID, ACCEPTED, SHIPPED, CANCEL_PENDING, RETURN_PENDING)
    }
}
