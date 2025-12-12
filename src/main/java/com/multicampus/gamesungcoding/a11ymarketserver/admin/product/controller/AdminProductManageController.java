package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.dto.AllProductInquireRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service.AdminProductManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.AdminProductsResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductAdminInquireResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminProductManageController {
    private final AdminProductManageService service;

    // 관리자 - 승인 대기중인 상품 조회
    @GetMapping("/v1/admin/products/pending")
    public ResponseEntity<List<ProductAdminInquireResponse>> inquirePendingProducts() {

        var list = this.service.inquirePendingProducts();
        return ResponseEntity.ok(list);
    }

    // 관리자 - 상품 상태 변경 (미구현)
    @PatchMapping("/v1/admin/products/{productId}/status")
    public ResponseEntity<String> changeProductStatus(@PathVariable String productId,
                                                      @RequestParam ProductStatus status) {

        this.service.changeProductStatus(UUID.fromString(productId), status);
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/v1/admin/products")
    public ResponseEntity<AdminProductsResponse> inquireAllProducts(@RequestParam(required = false) String query,
                                                                    @RequestParam(required = false) ProductStatus status,
                                                                    @RequestParam(defaultValue = "1") Integer page,
                                                                    @RequestParam(defaultValue = "20") Integer size) {

        var response = this.service.inquireAllProducts(
                new AllProductInquireRequest(
                        query,
                        status,
                        page,
                        size
                ));
        return ResponseEntity.ok(response);
    }
}
