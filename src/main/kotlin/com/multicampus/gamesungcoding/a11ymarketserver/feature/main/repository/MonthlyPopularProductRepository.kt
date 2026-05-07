package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MonthlyPopularProductRepository : JpaRepository<MonthlyPopularProduct, UUID> {
    fun findTop10ByOrderByRankingAsc(): List<MonthlyPopularProduct>
}
