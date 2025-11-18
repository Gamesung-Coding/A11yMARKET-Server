package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckoutResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCreateReqDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderCreateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final OrderService checkoutInfoService;
    private final OrderCreateService orderCreateService;

    // 결제 준비 (결제 정보 조회)
    @PostMapping("/v1/orders/pre-check")
    public OrderCheckoutResponse preCheck(
            @AuthenticationPrincipal Authentication authentication,
            @Valid @RequestBody OrderCheckRequest req
    ) {

        return checkoutInfoService.getCheckoutInfo(authentication.getName(), req);
    }

    // 주문 생성
    @PostMapping("v1/orders")
    public UUID createOrder(HttpSession session, @Valid @RequestBody OrderCreateReqDTO req) {
        UUID userId = (UUID) session.getAttribute("userId");
        return orderCreateService.createOrder(userId, req);
    }
}
