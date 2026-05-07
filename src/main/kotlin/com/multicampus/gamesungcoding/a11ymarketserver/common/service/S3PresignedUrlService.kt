package com.multicampus.gamesungcoding.a11ymarketserver.common.service

import com.github.f4b6a3.uuid.alt.GUID
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class S3PresignedUrlService(
    private val s3Presigner: S3Presigner,
    private val s3StorageProperties: S3StorageProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private val PRESIGN_DURATION: Duration = Duration.ofMinutes(15)
        private val ALLOWED_CONTENT_TYPES = setOf(
            "image/jpeg", "image/png", "image/webp", "image/gif"
        )
    }

    /**
     * 이미지 업로드용 Presigned PUT URL을 생성합니다.
     *
     * @param originalFileName 원본 파일명
     * @param contentType Content-Type (image/jpeg 등)
     * @param pathPrefix S3 key prefix (예: "images/{sellerId}/{productId}")
     * @return objectKey와 presigned URL 정보
     */
    fun generatePresignedPutUrl(
        originalFileName: String,
        contentType: String,
        pathPrefix: String
    ): PresignedUrlResult {
        require(contentType in ALLOWED_CONTENT_TYPES) {
            "허용되지 않는 Content-Type입니다: $contentType (허용: $ALLOWED_CONTENT_TYPES)"
        }

        val uniqueFileName = "${GUID.v7().toUUID()}_$originalFileName"
        val objectKey = "$pathPrefix/$uniqueFileName"
        val bucketName = s3StorageProperties.bucket

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .contentType(contentType)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(PRESIGN_DURATION)
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = s3Presigner.presignPutObject(presignRequest)

        log.debug("Generated presigned URL for key: {}, expires in: {}s", objectKey, PRESIGN_DURATION.seconds)

        // MinIO의 presigned URL은 endpoint 기반으로 자동 생성됨
        val uploadUrl = presignedRequest.url().toString()

        // 업로드 완료 후 접근 가능한 URL (S3 endpoint 기반)
        val imageUrl = "${s3StorageProperties.baseImageUrl}/$bucketName/$objectKey"

        return PresignedUrlResult(
            objectKey = objectKey,
            uploadUrl = uploadUrl,
            imageUrl = imageUrl,
            expiresInSeconds = PRESIGN_DURATION.seconds
        )
    }
}

data class PresignedUrlResult(
    val objectKey: String,
    val uploadUrl: String,
    val imageUrl: String,
    val expiresInSeconds: Long
)
