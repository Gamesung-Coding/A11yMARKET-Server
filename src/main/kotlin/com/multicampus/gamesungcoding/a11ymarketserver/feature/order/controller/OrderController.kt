package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCreateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Validated
class OrderController(
    private val orderService: OrderService
) {
    // 결제 준비 (결제 정보 조회)
    // @PostMapping("/v1/orders/pre-check")
    // @Deprecated("v2로 대체됨.")
    // fun preCheck() {
    //     throw UnsupportedOperationException("이 API는 더 이상 지원되지 않습니다. /api/v2/orders/pre-check를 사용해주세요.")
    // }

    // 주문 생성
    @PostMapping("/v1/orders")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody req: OrderCreateRequest,
        resp: HttpServletResponse
    ): OrderResponse {
        val orderResp = orderService.createOrder(userDetails.username, req)
        resp.setHeader(HttpHeaders.LOCATION, "/api/v1/users/me/orders/${orderResp.orderId}")
        return orderResp
    }
}
