package com.multicampus.gamesungcoding.a11ymarketserver.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 단일 엔드포인트로 목록/검색 처리.
 * - GET /api/v1/products
 * · 전체: /api/v1/products
 * · 키워드: /api/v1/products?search=건강
 * · (향후) 인증/등급: /api/v1/products?certified=true&grade=우수
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products")
    public List<ProductDTO> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean certified,
            @RequestParam(required = false) String grade
    ) {
        return productService.getProducts(search, certified, grade);
    }
}

