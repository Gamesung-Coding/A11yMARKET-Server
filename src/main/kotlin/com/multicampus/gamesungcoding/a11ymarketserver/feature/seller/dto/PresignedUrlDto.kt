package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Presigned URL 발급 요청
 */
data class ImageUploadInfo(
    @field:NotBlank
    val originalFileName: String,

    @field:NotBlank
    val contentType: String // image/jpeg, image/png, image/webp 등
)

data class PresignedUrlRequest(
    @field:Size(min = 1, max = 10, message = "이미지는 1~10개까지 업로드할 수 있습니다.")
    val images: List<ImageUploadInfo>
)

/**
 * Presigned URL 발급 응답
 */
data class PresignedUploadUrl(
    val objectKey: String,        // S3 object key (프론트가 상품 등록 시 이 값을 전달)
    val uploadUrl: String,        // presigned PUT URL (프론트가 여기에 직접 업로드)
    val imageUrl: String,         // 업로드 완료 후 접근 가능한 URL
    val expiresInSeconds: Long    // URL 유효 시간(초)
)

data class PresignedUploadResponse(
    val urls: List<PresignedUploadUrl>
)
