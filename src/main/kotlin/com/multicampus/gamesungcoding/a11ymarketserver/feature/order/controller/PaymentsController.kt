package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.PaymentVerifyRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.PaymentVerifyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
@Validated
class PaymentsController(
    private val orderService: OrderService
) {
    // 결제 검증
    @PostMapping("/verify")
    fun verifyPayment(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid req: PaymentVerifyRequest
    ): PaymentVerifyResponse =
        orderService.verifyPayment(userDetails.username, req)
}