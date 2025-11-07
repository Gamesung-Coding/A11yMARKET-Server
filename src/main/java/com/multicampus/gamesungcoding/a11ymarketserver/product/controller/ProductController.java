package com.multicampus.gamesungcoding.a11ymarketserver.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [ProductController]
 * - 상품 목록 조회 및 조건 검색 API
 * - 현재: Product 테이블만 사용 (SELLER DB 조인은 추후 예정)
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * [전체 상품 목록 조회]
     * - 요청: GET /api/v1/products
     * - 응답: 상품 목록(JSON 배열)
     */
    @GetMapping("/api/v1/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * [상품 조건 검색]
     * - 요청: GET /api/v1/products/filter?search=건강&certified=true&grade=인증
     * - 현재 기능:
     * 1. search(상품명 부분 일치 검색) → 실제 작동
     * 2. certified(인증여부), grade(판매자등급) → 나중에 SELLER DB 조인 시 반영 예정
     */

    @GetMapping("/api/v1/products/filter")
    public List<ProductDTO> getFilteredProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean certified,
            @RequestParam(required = false) String grade
    ) {
        return productService.getFilteredProducts(search, certified, grade);
    }
}

