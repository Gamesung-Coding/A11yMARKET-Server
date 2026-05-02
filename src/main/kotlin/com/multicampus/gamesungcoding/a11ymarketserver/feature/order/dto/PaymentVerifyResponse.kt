package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import java.time.LocalDateTime
import java.util.*

data class PaymentVerifyResponse(
    val orderId: UUID,
    val status: String,
    val amount: Int,
    val paidAt: LocalDateTime?
)
