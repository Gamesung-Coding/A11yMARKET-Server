package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.*

/**
 * V2 이미지 정보 (프론트가 Presigned URL로 업로드 완료 후 전달)
 */
data class ImageInfoV2(
    @field:NotBlank
    val objectKey: String,        // S3 object key (presigned URL 발급 시 받은 값)

    val altText: String? = null,

    @field:Min(0)
    val sequence: Int,

    val imageId: UUID? = null,    // 기존 이미지 수정 시에만 전달

    val isNew: Boolean = true     // 신규 이미지 여부
)

/**
 * V2 상품 등록 요청 (이미지 URL 참조 기반)
 */
data class SellerProductRegisterRequestV2(
    @field:NotBlank val productName: String,
    @field:NotBlank val productDescription: String,
    @field:NotBlank val categoryId: String,
    @field:Min(0) val productPrice: Int,
    @field:Min(0) val productStock: Int,
    val images: List<ImageInfoV2> = emptyList()
)

/**
 * V2 상품 수정 요청 (이미지 URL 참조 기반)
 */
data class SellerProductUpdateRequestV2(
    @field:NotBlank val productName: String,
    @field:NotBlank val productDescription: String,
    @field:NotBlank val categoryId: String,
    @field:Min(0) val productPrice: Int,
    @field:Min(0) val productStock: Int,
    val productStatus: ProductStatus,
    val images: List<ImageInfoV2> = emptyList()
)
