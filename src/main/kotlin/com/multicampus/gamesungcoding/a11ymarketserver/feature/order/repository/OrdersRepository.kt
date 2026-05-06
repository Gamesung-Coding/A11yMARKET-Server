package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface OrdersRepository : JpaRepository<Orders, UUID> {
    // 내 주문 목록
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<Orders>

    // 내 주문 상세
    fun findByOrderIdAndUserId(orderId: UUID, userId: UUID): Orders?

    // 관리자 주문 목록 조회 필터링
    @Query(
        """
            SELECT o FROM Orders o
            WHERE (:status IS NULL OR o.orderStatus = :status)
            AND (
                :searchType IS NULL OR :keyword IS NULL OR
                (
                    (:searchType = 'userName' AND LOWER(o.userName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    OR (:searchType = 'receiverName' AND LOWER(o.receiverName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    OR (:searchType = 'userPhone' AND o.userPhone LIKE CONCAT('%', :keyword, '%'))
                    OR (:searchType = 'receiverPhone' AND o.receiverPhone LIKE CONCAT('%', :keyword, '%'))
                    OR (:searchType = 'orderId' AND CAST(o.orderId AS string) LIKE CONCAT('%', :keyword, '%'))
                )
            )
            AND (:startDate IS NULL OR o.createdAt >= CAST(:startDate AS timestamp))
            AND (:endDate IS NULL OR o.createdAt <= CAST(:endDate AS timestamp))
            ORDER BY o.createdAt DESC
            
            """
    )
    fun searchOrders(
        @Param("searchType") searchType: String,
        @Param("keyword") keyword: String,
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String
    ): List<Orders>
}
