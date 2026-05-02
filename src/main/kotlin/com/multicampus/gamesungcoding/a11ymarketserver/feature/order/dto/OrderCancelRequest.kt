package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import jakarta.validation.constraints.NotEmpty

data class OrderCancelRequest(
    @field:NotEmpty val orderItemId: String,
    @field:NotEmpty val reason: String
)
