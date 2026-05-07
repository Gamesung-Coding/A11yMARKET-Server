package com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.service

import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.dto.AdminDashboardStats
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminDashboardService(
    private val sellerRepository: SellerRepository,
    private val productRepository: ProductRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getAdminDashboardStats(): AdminDashboardStats {
        val sellerPendingCount = sellerRepository.countBySellerSubmitStatus(SellerSubmitStatus.PENDING)
        val productPendingCount = productRepository.countByProductStatus(ProductStatus.PENDING)

        log.debug(
            "Fetched Admin Dashboard Stats - Pending Sellers: {}, Pending Products: {}",
            sellerPendingCount, productPendingCount
        )

        return AdminDashboardStats(sellerPendingCount, productPendingCount)
    }
}
