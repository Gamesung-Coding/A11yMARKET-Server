package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto

import java.math.BigDecimal
import java.util.*

data class SellerDashboardResponse(
    val sellerId: UUID,
    val sellerName: String,
    val sellerIntro: String?,
    val totalRevenue: Int?,
    val totalOrderCount: Long?,
    val refundRate: BigDecimal?,
    val confirmedRate: BigDecimal?
)
