package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus

data class AllProductInquireRequest(
    val query: String?,
    val status: ProductStatus?,
    val page: Int,
    val size: Int
)
