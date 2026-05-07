package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus

data class AllProductInquireRequest(
    val query: String = "",
    val status: ProductStatus = ProductStatus.APPROVED,
    val page: Int = 0,
    val size: Int = 20
)
