package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SellerRepository : JpaRepository<Seller, UUID> {
    fun findByUserUserId(userId: UUID): Seller?

    // sellerSubmitStatus가 pending인 만매자 조회
    fun findAllBySellerSubmitStatus(status: SellerSubmitStatus): List<Seller>

    fun existsByUserUserId(userId: UUID): Boolean

    fun countBySellerSubmitStatus(status: SellerSubmitStatus): Int
}
