package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties
import com.multicampus.gamesungcoding.a11ymarketserver.common.service.S3PresignedUrlService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService
import io.awspring.cloud.s3.S3Template
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SellerServiceV2(
    private val s3PresignedUrlService: S3PresignedUrlService,
    private val s3Template: S3Template,
    private val s3StorageProperties: S3StorageProperties,

    private val sellerRepository: SellerRepository,
    private val productRepository: ProductRepository,
    private val productImagesRepository: ProductImagesRepository,
    private val productAnalysisService: ProductAnalysisService,
    private val productAiSummaryRepository: ProductAiSummaryRepository,
    private val categoryRepository: CategoryRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 이미지 업로드용 Presigned URL 발급
     */
    @Transactional(readOnly = true)
    fun generateUploadUrls(userId: UUID, request: PresignedUrlRequest): PresignedUploadResponse {
        val seller = sellerRepository.findByUserUserId(userId)
            ?: throw DataNotFoundException("판매자 정보가 존재하지 않습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 이미지를 업로드할 수 있습니다.")
        }

        val sellerId = seller.sellerId
            ?: throw DataNotFoundException("판매자 ID가 존재하지 않습니다.")

        val urls = request.images.map { imageInfo ->
            val pathPrefix = "images/$sellerId"
            val result = s3PresignedUrlService.generatePresignedPutUrl(
                originalFileName = imageInfo.originalFileName,
                contentType = imageInfo.contentType,
                pathPrefix = pathPrefix
            )

            PresignedUploadUrl(
                objectKey = result.objectKey,
                uploadUrl = result.uploadUrl,
                imageUrl = result.imageUrl,
                expiresInSeconds = result.expiresInSeconds
            )
        }

        return PresignedUploadResponse(urls)
    }

    /**
     * V2 상품 등록 (이미지 URL 참조 기반)
     */
    fun registerProduct(userId: UUID, request: SellerProductRegisterRequestV2): ProductDetailResponse {
        val seller = sellerRepository.findByUserUserId(userId)
            ?: throw DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.")
        }

        val category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId))

        var product = Product(
            seller,
            category,
            request.productPrice,
            request.productStock,
            request.productName,
            request.productDescription,
            ProductStatus.PENDING
        )

        product = productRepository.save(product)

        // 이미지 메타데이터 저장 (프론트가 이미 S3에 업로드 완료)
        val savedImages = if (request.images.isNotEmpty()) {
            saveImageMetadata(request.images, product)
        } else {
            null
        }

        // AI 분석 (이미지 파일 없이 텍스트 기반으로만)
        val aiSummary = productAiSummaryRepository.save(
            createAiSummaryForProduct(
                product.productId!!,
                product.productName,
                product.productDescription
            )
        )

        return product.toDetailResponse(savedImages, aiSummary)
    }

    /**
     * V2 상품 수정 (이미지 URL 참조 기반)
     */
    fun updateProduct(userId: UUID, productId: UUID, request: SellerProductUpdateRequestV2): ProductDTO {
        val seller = sellerRepository.findByUserUserId(userId)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.")
        }

        val product = productRepository.findByIdOrNull(productId)
            ?: throw DataNotFoundException("상품 정보를 찾을 수 없습니다.")

        if (product.seller?.sellerId != seller.sellerId) {
            throw InvalidRequestException("본인의 상품만 수정할 수 있습니다.")
        }

        val category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId))

        product.updateBySeller(
            category,
            request.productName,
            request.productDescription,
            request.productPrice,
            request.productStock,
            request.productStatus
        )

        // 이미지 처리
        val dbImages = product.productImages
        val requestImages = request.images

        // 삭제할 이미지: 요청에 없는 기존 이미지
        val requestImageIds = requestImages.mapNotNull { it.imageId }.toSet()
        val imagesToDelete = dbImages.filter { it.imageId !in requestImageIds }

        imagesToDelete.forEach { img ->
            deleteImageFromS3(img)
            product.productImages.remove(img)
        }

        // 신규/수정 이미지 처리
        requestImages.forEach { imgInfo ->
            if (imgInfo.isNew) {
                // 새 이미지: objectKey 기반으로 메타데이터만 저장
                val savedImage = productImagesRepository.save(
                    ProductImages(
                        product = product,
                        imageUrl = imgInfo.objectKey,  // S3 object key를 URL로 저장
                        altText = imgInfo.altText,
                        imageSequence = imgInfo.sequence
                    )
                )
                product.productImages.add(savedImage)
            } else {
                // 기존 이미지: 메타데이터만 업데이트
                val existsImage = dbImages.find { it.imageId == imgInfo.imageId }
                    ?: throw DataNotFoundException("기존 이미지 정보를 찾을 수 없습니다.")
                existsImage.altText = imgInfo.altText
                existsImage.imageSequence = imgInfo.sequence
            }
        }

        return productRepository.save(product).toDTO()
    }

    /**
     * 이미지 메타데이터를 DB에 저장 (S3 업로드는 프론트에서 완료)
     */
    private fun saveImageMetadata(
        images: List<ImageInfoV2>,
        product: Product
    ): List<ProductImages> {
        return images.map { imgInfo ->
            productImagesRepository.save(
                ProductImages(
                    product = product,
                    imageUrl = imgInfo.objectKey,  // presigned URL 발급 시 반환한 objectKey
                    altText = imgInfo.altText,
                    imageSequence = imgInfo.sequence
                )
            )
        }
    }

    /**
     * S3에서 이미지 삭제
     */
    private fun deleteImageFromS3(img: ProductImages) {
        val objectKey = img.imageUrl ?: return

        try {
            s3Template.deleteObject(s3StorageProperties.bucket, objectKey)
            log.debug("Deleted image from S3: bucket={}, key={}", s3StorageProperties.bucket, objectKey)
            productImagesRepository.delete(img)
        } catch (e: Exception) {
            log.error("Failed to delete image from S3: {}", objectKey, e)
        }
    }

    /**
     * AI Summary 생성 (텍스트 기반)
     */
    private fun createAiSummaryForProduct(
        productId: UUID,
        productName: String?,
        productDescription: String?
    ): ProductAiSummary {
        val result = productAnalysisService.analysisProductImage(
            productName,
            productDescription,
            null // V2에서는 이미지 파일 없이 텍스트 기반으로만 분석
        )

        return ProductAiSummary(
            productRepository.getReferenceById(productId),
            summaryText = result?.summary ?: "",
            usageContext = result?.usageContext ?: "",
            usageMethod = result?.usageMethod ?: ""
        )
    }
}
