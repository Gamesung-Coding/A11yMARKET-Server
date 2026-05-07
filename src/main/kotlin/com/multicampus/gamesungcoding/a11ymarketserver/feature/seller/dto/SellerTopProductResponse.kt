package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto

import java.util.*

data class SellerTopProductResponse(
    val sellerId: UUID,
    val productId: UUID,
    val productName: String,
    val productPrice: Int,
    val productImageUrl: String?,
    val orderCount: Long,
    val totalQuantitySold: Long,
    val totalSalesAmount: Int,
    val salesRank: Int
)
