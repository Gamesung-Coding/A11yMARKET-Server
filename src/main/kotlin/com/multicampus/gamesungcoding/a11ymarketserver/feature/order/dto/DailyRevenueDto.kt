package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class DailyRevenueDto(
    val orderDate: LocalDateTime,
    val dailyRevenue: BigDecimal
)
