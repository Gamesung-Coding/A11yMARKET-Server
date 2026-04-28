package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderSheetRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderSheetResponse
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
@RequestMapping("/api/v2/orders")
@Validated
class OrderControllerV2(
    private val orderService: OrderService
) {
    @PostMapping("/pre-check")
    fun getOrderSheet(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody req: OrderSheetRequest
    ): OrderSheetResponse =
        orderService.getOrderSheet(userDetails.username, req)
}