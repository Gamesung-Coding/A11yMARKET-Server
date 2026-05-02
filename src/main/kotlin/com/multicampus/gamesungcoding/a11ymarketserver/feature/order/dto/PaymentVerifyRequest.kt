package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class PaymentVerifyRequest(
    val orderId: String,

    @field:Positive(message = "결제 금액은 0보다 커야 합니다.")
    val amount: Int,

    @field:NotBlank(message = "결제 방식은 필수입니다.")
    val method: String,

    val paymentKey: String? = null,
    val impUid: String? = null,
    val cartItemIdsToDelete: List<String>? = null
)
