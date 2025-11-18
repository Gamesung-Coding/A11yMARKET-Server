package com.multicampus.gamesungcoding.a11ymarketserver.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerProductRegisterRequest;

import java.util.UUID;

/**
 * 판매자 비즈니스 로직 인터페이스
 */
public interface SellerService {

    /**
     * 판매자 가입 신청 처리
     */
    SellerApplyResponse applySeller(String userId, SellerApplyRequest request);

    /**
     * 판매자 상품 등록 신청 처리
     */
    ProductDTO registerProduct(UUID userId, SellerProductRegisterRequest request);
}