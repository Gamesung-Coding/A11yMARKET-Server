package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.controller;


import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class CartController {

    private final CartService cartService;

    // GET /api/v1/cart 목록 조회 기능
    @GetMapping("/v1/cart/me")
    public ResponseEntity<CartItemListResponse> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<CartItemResponse> items = cartService.getCartItems(userDetails.getUsername());
        int total = cartService.getCartTotal(userDetails.getUsername());

        CartItemListResponse body = CartItemListResponse.builder()
                .items(items)
                .total(total)
                .build();

        return ResponseEntity.ok(body);
    }

    // POST /api/v1/cart/items 상품 추가 기능
    @PostMapping("/v1/cart/items")
    public ResponseEntity<CartItemResponse> addItem(
            @Valid @RequestBody CartAddRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        CartItemResponse created = cartService.addItem(req, userDetails.getUsername());
        return ResponseEntity
                .created(URI.create("/api/v1/cart/items/" + created.cartItemId()))
                .body(created);
    }

    // PATCH /api/v1/cart/items/{cartItemId} 수량 조정 기능
    @PatchMapping("/v1/cart/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(
            @PathVariable @NotNull String cartItemId,
            @Valid @RequestBody CartQtyUpdateRequest body,
            @AuthenticationPrincipal UserDetails userDetails) {

        CartItemResponse updated = cartService.updateQuantity(
                UUID.fromString(cartItemId),
                body.quantity(),
                userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/v1/cart/items 상품 삭제 기능
    @DeleteMapping("/v1/cart/items")
    public ResponseEntity<Void> deleteItems(
            @Valid @RequestBody CartItemDeleteRequest itemIds,
            @AuthenticationPrincipal UserDetails userDetails) {

        cartService.deleteItems(itemIds, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}