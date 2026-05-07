package com.multicampus.gamesungcoding.a11ymarketserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.s3")
data class S3StorageProperties(
    val bucket: String,
    val endpoint: String,
    val region: String,
    val accessKey: String,
    val secretKey: String,
    val cdnUrl: String? = null
) {
    init {
        require(bucket.isNotBlank()) { "S3 버킷 이름은 비어 있을 수 없습니다." }
        require(endpoint.startsWith("http")) { "S3 endpoint는 http 또는 https로 시작해야 합니다." }
    }

    /**
     * 이미지 접근 URL의 base를 반환합니다.
     * cdnUrl이 설정되어 있으면 CDN URL을, 아니면 S3 endpoint를 사용합니다.
     */
    val baseImageUrl: String
        get() = cdnUrl ?: endpoint
}
