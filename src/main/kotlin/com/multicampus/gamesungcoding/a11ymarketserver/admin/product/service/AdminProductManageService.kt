package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service

import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto.AllProductInquireRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.AdminProductsResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductAdminInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AdminProductManageService(
    private val productRepository: ProductRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun inquirePendingProducts(): List<ProductAdminInquireResponse> {
        val list = productRepository.findAll { root, _, criteriaBuilder ->
            // !! column 이름이 아니라 entity 필드 이름으로 작성해야 함 !!
            criteriaBuilder.equal(root.get<String>("productStatus"), "PENDING")
        }

        if (list.isEmpty()) {
            log.info("inquirePendingProducts - list is empty")
            return emptyList()
        }

        return list.map { ProductAdminInquireResponse.fromEntity(it) }
    }

    // 관리자 - 상품 상태 변경
    @Transactional
    fun changeProductStatus(productId: UUID, status: ProductStatus) {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw IllegalArgumentException("Product not found")
        product.changeStatus(status)
    }

    fun inquireAllProducts(req: AllProductInquireRequest): AdminProductsResponse {
        val pageable = PageRequest.of(req.page, req.size)
        val products = productRepository.findAllByQuery(req.query, req.status, pageable)
        val productResponses = products.content.map { ProductAdminInquireResponse.fromEntity(it) }

        return AdminProductsResponse(
            productResponses.size,
            productResponses
        )
    }
}
