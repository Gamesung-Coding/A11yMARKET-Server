package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCancelRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderConfirmRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/me/orders")
@Validated
class UserOrderController(
    private val orderService: OrderService
) {
    // 내 주문 목록 조회
    @GetMapping("/")
    fun getMyOrders(
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<OrderResponse> =
        orderService.getMyOrders(UUID.fromString(userDetails.username))


    // 내 주문 상세 조회
    @GetMapping("/{orderItemId}")
    fun getMyOrderDetail(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable orderItemId: String
    ): OrderDetailResponse =
        runCatching {
            val orderItemUuid = UUID.fromString(orderItemId)
            orderService.getMyOrderDetail(orderItemUuid, UUID.fromString(userDetails.username))
        }.getOrElse { throw InvalidRequestException("잘못된 UUID 형식입니다.") }


    // 주문 취소
    @PostMapping("/cancel-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelOrderItems(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid req: OrderCancelRequest
    ) = orderService.cancelOrderItems(UUID.fromString(userDetails.username), req)


    // 주문 확정
    @PostMapping("/items/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmOrderItems(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid req: OrderConfirmRequest
    ) = orderService.confirmOrderItems(UUID.fromString(userDetails.username), req)
}
