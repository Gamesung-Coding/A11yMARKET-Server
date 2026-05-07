package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderSearchRequest
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.toAdminResponse
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper.toDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AdminOrderService(
    private val ordersRepository: OrdersRepository,
    private val orderItemsRepository: OrderItemsRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getOrders(request: AdminOrderSearchRequest): List<AdminOrderResponse> {
        val results = ordersRepository.searchOrders(
            request.searchType,
            request.keyword,
            request.startDate,
            request.endDate
        )
        return results.map { it.toAdminResponse() }
    }

    fun getOrderDetails(orderItemId: UUID): OrderDetailResponse {
        val item = orderItemsRepository.findByIdOrNull(orderItemId)
            ?: throw DataNotFoundException("Order item not found")
        return item.toDetailResponse()
    }

    @Transactional
    fun updateOrderItemStatus(orderItemId: UUID, status: OrderItemStatus) {
        val orderItem = orderItemsRepository.findByIdOrNull(orderItemId)
            ?: throw DataNotFoundException("Order not found")
        orderItem.updateOrderItemStatus(status)
    }
}
