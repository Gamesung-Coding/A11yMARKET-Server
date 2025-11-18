package com.multicampus.gamesungcoding.a11ymarketserver.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 판매자 관련 API 엔드포인트
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerController {

    private final SellerService sellerService;

    /**
     * 테스트용 회원/판매자 userId 하드코딩
     * - 실제 운용 시에는 제거하고 세션 또는 인증 정보에서 가져와야 함
     * - 추후 테스트용 코드 삭제 예정
     */
    private static final String TEST_APPLY_USER_ID = "019A698A-43EA-7B27-9103-A70F0065184A";   // 판매자 가입용 (아직 sellers에 없는 user)
    private static final String TEST_SELLER_USER_ID = "019A698A-43EA-7DCA-8B34-CDE9A3850ADB";  // 이미 sellers에 존재하는 user

    /**
     * 판매자 가입 신청
     * POST /api/v1/seller/apply
     */
    @PostMapping("/v1/seller/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SellerApplyResponse> applySeller(
            //HttpSession session,
            @RequestParam String userIdString,
            @RequestBody @Valid SellerApplyRequest request
    ) {
        // 테스트용 - 세션/파라미터가 없으면 테스트용 userId로 대체
        if (userIdString == null || userIdString.isBlank()) {
            userIdString = TEST_APPLY_USER_ID;
        }

        //        테스트 완료시 주석 제거
        //        if (userIdString == null) {
        //            return ResponseEntity.notFound().build();
        //        }

        UUID userId = UUID.fromString(userIdString);

        SellerApplyResponse response = sellerService.applySeller(userId, request); //테스트
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 테스트

        //        return ResponseEntity.ok(sellerService.applySeller(userId, request));
    }

    /**
     * 판매자 상품 등록 신청
     * POST /api/v1/seller/products
     */
    @PostMapping("/v1/seller/products")
    public ResponseEntity<ProductDTO> registerProduct(
            @RequestParam String userIdString,
            @RequestBody @Valid SellerProductRegisterRequest request
    ) {
        // 테스트용 - 세션/파라미터가 없으면 테스트용 "이미 판매자인 userId"로 대체(추후 삭제)
        if (userIdString == null || userIdString.isBlank()) {
            userIdString = TEST_SELLER_USER_ID;
        }

        //        // 테스트 완료시 주석 제거
        //        if (userIdString == null) {
        //            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //        }

        UUID userId = UUID.fromString(userIdString);

        ProductDTO response = sellerService.registerProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}