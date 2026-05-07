package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto

import java.util.*

data class CategoryRecommendResponse(
    var categoryId: UUID,
    var categoryName: String
) {
    val products: MutableList<CatProductInfo> = mutableListOf()

    fun addProduct(product: CatProductInfo) {
        this.products.add(product)
    }
}
