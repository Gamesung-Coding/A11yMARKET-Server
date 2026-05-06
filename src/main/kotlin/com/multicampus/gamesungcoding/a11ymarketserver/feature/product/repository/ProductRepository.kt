package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 목록/검색용 Repository.
 * - JPQL 사용: DB 컬럼명 변경에 대한 내성 확보
 * - 문자열 연결은 CONCAT 중첩으로 '%search%' 구성 (JPQL 제약)
 */
@Repository
interface ProductRepository : JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    // 특정 판매자의 상품 전체 조회
    fun findBySellerSellerId(sellerId: UUID, pageable: Pageable): Page<Product>

    fun findAllBySellerUserUserId(userId: UUID): List<Product>

    fun countByProductStatus(productStatus: ProductStatus): Int

    @Query(
        """
            SELECT p FROM Product p
            WHERE (:query IS NULL OR
                   LOWER(p.productName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :query, '%')))
              AND (:status IS NULL OR p.productStatus = :status)
            
            """
    )
    fun findAllByQuery(
        @Param("query") query: String,
        @Param("status") status: ProductStatus,
        pageable: Pageable
    ): Page<Product>
}
