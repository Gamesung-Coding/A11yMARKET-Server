package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.service

import com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.model.AdminSellerUpdateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerApplyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toApplyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AdminSellerService(
    private val sellerRepository: SellerRepository,
    private val ordersRepository: OrdersRepository,
    private val orderItemsRepository: OrderItemsRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getAllSellerProfile(): List<SellerProfileResponse> =
        sellerRepository.findAll().map { it.toProfileResponse() }

    fun getSellerProfile(sellerId: UUID): SellerDetailResponse {
        val seller = sellerRepository.findByIdOrNull(sellerId)
            ?: throw DataNotFoundException("Seller not found")

        val orderItems = orderItemsRepository.findAllByProductSeller(seller)
        return seller.toDetailResponse(orderItems)
    }

    fun inquirePendingSellers(): List<SellerApplyResponse> {
        val pendingList = sellerRepository.findAllBySellerSubmitStatus(SellerSubmitStatus.PENDING)
        return pendingList.map { it.toApplyResponse() }
    }

    @Transactional
    fun updateSellerStatus(sellerId: UUID, status: SellerSubmitStatus) {
        val seller = sellerRepository.findByIdOrNull(sellerId)
            ?: throw DataNotFoundException("Seller not found")

        when (status) {
            SellerSubmitStatus.APPROVED -> seller.approve()
            SellerSubmitStatus.REJECTED -> seller.reject()
            else -> {} // PENDING 등 다른 상태는 무시
        }
    }

    @Transactional
    fun updateSellerInfo(sellerId: UUID, request: AdminSellerUpdateRequest) {
        val seller = sellerRepository.findByIdOrNull(sellerId)
            ?: throw DataNotFoundException("Seller not found")

        seller.updateAdminSellerInfo(
            request.sellerName,
            request.businessNumber,
            request.sellerIntro,
            request.sellerGrade,
            request.a11yGuarantee
        )
    }
}
