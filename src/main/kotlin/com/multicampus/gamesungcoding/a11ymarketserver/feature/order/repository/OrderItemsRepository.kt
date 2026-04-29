package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface OrderItemsRepository : JpaRepository<OrderItems, UUID> {
    // 특정 orderId 주문의 모든 상품 조회
    fun findAllByOrderOrderId(orderId: UUID?): List<OrderItems>

    fun findAllByProductSellerUserUserEmailAndOrderItemStatusOrderByOrderCreatedAtDesc(
        userEmail: String?,
        status: OrderItemStatus?,
        pageable: Pageable?
    ): Page<OrderItems>

    fun existsByOrderItemIdAndProductSeller(orderItemId: UUID, seller: Seller): Boolean

    fun findAllByProductSellerUserUserEmailAndOrderItemStatusIn(
        userEmail: String,
        statuses: List<OrderItemStatus>
    ): List<OrderItems>

    fun findAllByProductSellerUserUserEmailOrderByOrderCreatedAtDesc(
        userEmail: String,
        pageable: Pageable
    ): Page<OrderItems>

    fun existsByProductSellerUserUserEmailAndOrderItemStatusIn(
        userEmail: String,
        statuses: List<OrderItemStatus>
    ): Boolean

    @Query(
        value = """
            SELECT
                FUNCTION('DATE_TRUNC', 'day', o.createdAt) as orderDate,
                SUM(oi.productPrice * oi.productQuantity) as dailyRevenue
            FROM OrderItems oi
            JOIN Orders o ON oi.order.orderId = o.orderId
            JOIN Product p ON oi.product.productId = p.productId
            WHERE p.seller.sellerId = :sellerId
              AND oi.orderItemStatus = 'CONFIRMED'
              AND YEAR(o.createdAt) = :year
              AND MONTH(o.createdAt) = :month
            GROUP BY FUNCTION('DATE_TRUNC', 'day', o.createdAt)
            ORDER BY FUNCTION('DATE_TRUNC', 'day', o.createdAt)
            
            """
    )
    fun findDailyRevenue(
        @Param("sellerId") sellerId: UUID,
        @Param("year") year: Int,
        @Param("month") month: Int
    ): List<Array<Any?>>

    @Query(
        value = """
             SELECT oi
             FROM OrderItems oi
             JOIN FETCH oi.product p
             JOIN FETCH oi.order o
             WHERE p.seller.sellerId = :sellerId
             ORDER BY o.createdAt DESC
            
            """,
        countQuery = """
                     SELECT count(oi) FROM OrderItems oi
                     JOIN oi.product p
                     WHERE p.seller.sellerId = :sellerId
                    
                    """
    )
    fun findBySellerIdWithDetails(
        @Param("sellerId") sellerId: UUID,
        pageable: Pageable
    ): Page<OrderItems>

    fun findAllByProductSeller(seller: Seller): List<OrderItems>

    fun countAllByProductSellerUserUserEmailAndOrderItemStatus(
        userEmail: String,
        orderItemStatus: OrderItemStatus
    ): Int

    fun countAllByProductSellerUserUserEmail(userEmail: String): Int

    @Query(
        """
             SELECT  oi.orderItemStatus, COUNT(oi)
             FROM OrderItems oi
             JOIN oi.product p
             JOIN p.seller s
             JOIN s.user u
             WHERE u.userEmail = :userEmail
             GROUP BY oi.orderItemStatus
            
            """
    )
    fun countOrderItemsByStatusGroupedBySellerUserEmail(@Param("userEmail") userEmail: String): List<Array<Any?>>
}
