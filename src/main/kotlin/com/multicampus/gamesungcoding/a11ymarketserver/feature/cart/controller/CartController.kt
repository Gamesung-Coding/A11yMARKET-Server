package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service.CartService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/cart")
@Validated
class CartController(
    private val cartService: CartService
) {
    // GET /api 목록 조회 기능
    @GetMapping("/me")
    fun getCart(
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemListResponse = cartService.getCartItems(UUID.fromString(userDetails.username))

    @GetMapping("/me/items/count")
    fun getCartItemCount(
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemCountResponse = cartService.getCartItemCount(UUID.fromString(userDetails.username))

    // POST /api/items 상품 추가 기능
    @PostMapping("/me/items")
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(
        @Valid @RequestBody req: @Valid CartAddRequest,
        @AuthenticationPrincipal userDetails: UserDetails,
        httpResponse: HttpServletResponse
    ): CartItemUpdatedResponse {
        httpResponse.setHeader(HttpHeaders.LOCATION, "/api/me")
        return cartService.addItem(req, UUID.fromString(userDetails.username))
    }

    // PATCH /api/items/{cartItemId} 수량 조정 기능
    @PatchMapping("/me/items/{cartItemId}")
    fun updateQuantity(
        @PathVariable @NotNull cartItemId: @NotNull String,
        @Valid @RequestBody body: @Valid CartQtyUpdateRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemUpdatedResponse = cartService.updateQuantity(
        UUID.fromString(cartItemId),
        body.quantity,
        UUID.fromString(userDetails.username)
    )

    // DELETE /api/items 상품 삭제 기능
    @DeleteMapping("/me/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItems(
        @Valid @RequestBody itemIds: @Valid CartItemDeleteRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) {
        cartService.deleteItems(itemIds, UUID.fromString(userDetails.username))
    }
}