package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import java.util.*

data class ProductResponse(
    val productId: UUID,
    val productName: String,
    val productDescription: String,
    val sellerName: String,
    val isA11yGuarantee: Boolean,
    val productPrice: Int,
    val productImages: List<ProductImageResponse?>,
    val parentCategoryId: UUID,
    val categoryId: UUID,
    val categoryName: String
)
