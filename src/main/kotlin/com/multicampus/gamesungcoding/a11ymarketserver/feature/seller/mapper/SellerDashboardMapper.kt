package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerTopProduct
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerTopProductResponse

fun SellerTopProduct.toTopProductResponse(): SellerTopProductResponse {
    return SellerTopProductResponse(
        sellerId = this.id.sellerId,
        productId = this.id.productId,
        productName = this.productName,
        productPrice = this.productPrice.toInt(),
        productImageUrl = this.productImageUrl,
        orderCount = this.orderCount,
        totalQuantitySold = this.totalQuantitySold,
        totalSalesAmount = this.totalSalesAmount,
        salesRank = this.salesRank
    )
}
