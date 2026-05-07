package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.controller

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderSearchRequest
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service.AdminOrderService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class AdminOrderManageController(
    private val adminOrderService: AdminOrderService
) {
    // 관리자 - 전체 주문 조회
    @GetMapping("/v1/admin/orders")
    fun inquireAllOrders(request: AdminOrderSearchRequest): List<AdminOrderResponse> =
        adminOrderService.getOrders(request)

    // 관리자 - 특정 주문 조회
    @GetMapping("/v1/admin/orders/{orderId}")
    fun inquireOrderDetails(@PathVariable orderId: String): OrderDetailResponse =
        adminOrderService.getOrderDetails(UUID.fromString(orderId))

    // 관리자 - 주문 상태 변경
    @PatchMapping("/v1/admin/orders/items/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeOrderStatus(
        @PathVariable orderItemId: String,
        @RequestParam status: OrderItemStatus
    ) = adminOrderService.updateOrderItemStatus(UUID.fromString(orderItemId), status)
}
