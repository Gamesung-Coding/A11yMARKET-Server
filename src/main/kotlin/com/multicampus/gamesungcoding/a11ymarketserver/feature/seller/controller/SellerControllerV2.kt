package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.PresignedUploadResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.PresignedUrlRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductRegisterRequestV2
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductUpdateRequestV2
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerServiceV2
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v2/seller")
class SellerControllerV2(
    private val sellerServiceV2: SellerServiceV2
) {
    /**
     * Presigned URL 발급
     *
     * @return 프론트엔드는 이 URL로 이미지를 직접 S3/MinIO에 업로드합니다.
     */
    @PostMapping("/upload-urls")
    fun getUploadUrls(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid request: PresignedUrlRequest
    ): PresignedUploadResponse =
        sellerServiceV2.generateUploadUrls(UUID.fromString(userDetails.username), request)

    /**
     * V2 상품 등록 (이미지 URL 참조 기반)
     *
     * 프론트엔드가 presigned URL로 이미지를 업로드 완료한 후,
     * objectKey를 포함하여 상품 정보를 등록합니다.
     */
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerProduct(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid request: SellerProductRegisterRequestV2
    ): ProductDetailResponse =
        sellerServiceV2.registerProduct(UUID.fromString(userDetails.username), request)

    /**
     * V2 상품 수정 (이미지 URL 참조 기반)
     */
    @PutMapping("/products/{productId}")
    fun updateProduct(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable productId: String,
        @RequestBody @Valid request: SellerProductUpdateRequestV2
    ): ProductDTO =
        sellerServiceV2.updateProduct(
            UUID.fromString(userDetails.username),
            UUID.fromString(productId),
            request
        )
}