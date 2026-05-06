package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerTopProduct
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SellerTopProductRepository : JpaRepository<SellerTopProduct, UUID> {
    fun findAllByIdSellerIdAndSalesRankLessThanEqualOrderBySalesRankAsc(
        idSellerId: UUID,
        salesRank: Int
    ): List<SellerTopProduct>
}
