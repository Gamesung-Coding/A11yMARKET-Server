package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller

import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto.AllProductInquireRequest
import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service.AdminProductManageService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.AdminProductsResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductAdminInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class AdminProductManageController(
    private val service: AdminProductManageService
) {
    // 관리자 - 승인 대기중인 상품 조회
    @GetMapping("/v1/admin/products/pending")
    fun inquirePendingProducts(): List<ProductAdminInquireResponse> =
        service.inquirePendingProducts()

    // 관리자 - 상품 상태 변경
    @PatchMapping("/v1/admin/products/{productId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProductStatus(
        @PathVariable productId: String,
        @RequestParam status: ProductStatus
    ) = service.changeProductStatus(UUID.fromString(productId), status)

    @GetMapping("/v1/admin/products")
    fun inquireAllProducts(
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) status: ProductStatus?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): AdminProductsResponse = service.inquireAllProducts(
        AllProductInquireRequest(query ?: "", status ?: ProductStatus.APPROVED, page, size)
    )
}
