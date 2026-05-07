package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerTopProductResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerTopProduct

fun SellerTopProduct.toTopProductResponse(): SellerTopProductResponse {
    return SellerTopProductResponse(
        sellerId = this.id?.sellerId ?: throw InvalidRequestException("Seller ID is missing"),
        productId = this.id?.productId ?: throw InvalidRequestException("Product ID is missing"),
        productName = this.productName,
        productPrice = this.productPrice.toInt(),
        productImageUrl = this.productImageUrl,
        orderCount = this.orderCount,
        totalQuantitySold = this.totalQuantitySold,
        totalSalesAmount = this.totalSalesAmount,
        salesRank = this.salesRank
    )
}
