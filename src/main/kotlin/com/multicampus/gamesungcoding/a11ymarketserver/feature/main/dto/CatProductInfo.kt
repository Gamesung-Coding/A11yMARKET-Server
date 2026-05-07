package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto

import java.util.*

data class CatProductInfo(
    val productId: UUID,
    val productName: String,
    val productPrice: Int,
    val productImageUrl: String
)
