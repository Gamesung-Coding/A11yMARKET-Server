package com.multicampus.gamesungcoding.a11ymarketserver.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.CheckoutInfoResponseDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.service.CheckoutInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final CheckoutInfoService checkoutInfoService;

    //결제 준비 (결제 정보 조회)
    @PostMapping("/v1/orders/pre-check")
    public CheckoutInfoResponseDTO preCheck(@RequestParam UUID userId) {
        return checkoutInfoService.getCheckoutInfo(userId);
    }
}
