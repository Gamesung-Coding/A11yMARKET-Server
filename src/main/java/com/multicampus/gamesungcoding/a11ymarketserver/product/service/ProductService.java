package com.multicampus.gamesungcoding.a11ymarketserver.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;

import java.util.List;

/**
 * [ProductService]
 * - 상품 목록 조회 기능 정의
 */
public interface ProductService {

    /**
     * 상품 전체 목록 조회
     */
    List<ProductDTO> getAllProducts();

    /**
     * 상품 조건 검색
     *
     * @param search    상품명 (부분 검색)
     * @param certified 판매자 접근성 인증 여부 (SELLER DB 조인 후 적용)
     * @param grade     판매자 등급 (SELLER DB 조인 후 적용)
     */
    List<ProductDTO> getFilteredProducts(String search, Boolean certified, String grade);
}
