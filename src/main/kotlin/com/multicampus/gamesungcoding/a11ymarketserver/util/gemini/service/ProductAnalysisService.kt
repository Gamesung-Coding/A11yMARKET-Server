package com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service

import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.model.Media
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

@Service
class ProductAnalysisService(
    builder: ChatClient.Builder
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val chatClient: ChatClient = builder.build()

    fun analysisProductImage(
        productName: String?,
        userDescription: String?,
        images: MutableList<MultipartFile>?
    ): ProductAnalysisResult? {
        // 이미지 업로드 테스트 이후 적용
        val prompt = String.format(
            """
                        당신은 쇼핑몰 상품 등록 도우미입니다.
                        제공된 상품 이미지를 분석하여 다음 정보를 한국어 및 존댓말로 작성해주세요.
                        
                        [분석 지침]
                        1. **판매자의 설명**을 가장 우선적으로 참고하세요.
                        2. 설명에 없는 정보는 **이미지**를 보고 추론하세요.
                        3. 이미지가 여러장일 경우, 모든 이미지를 종합적으로 분석하세요.
                        4. 불확실한 정보는 추측하지 말고 "정보 없음"으로 작성하세요.
                        
                        [상품명]
                        %s
                        
                        [판매자 설명]
                        %s
                        
                        위 내용을 바탕으로 아래 항목을 JSON으로 추출하세요.
                        1. 상품에 대한 간단한 한 줄 요약
                        2. 사용처(어디에, 어떤 상황에서 사용하는지).
                        3. 사용 방법 (영양제라면 복용법, 기계라면 간단한 작동법, 의류라면 세탁 및 관리법 등).
                        
                        """.trimIndent(),
            productName,
            if (userDescription == null || userDescription.isBlank())
                "정보 없음 (이미지만 참고)"
            else
                userDescription
        )

        val mediaList: MutableList<Media?> = ArrayList<Media?>()
        if (images != null) {
            for (image in images) {
                try {
                    val resizedBytes = resizeImage(image, 1024, "png")
                    mediaList.add(
                        Media(
                            MimeTypeUtils.IMAGE_PNG,
                            ByteArrayResource(resizedBytes)
                        )
                    )
                } catch (err: IOException) {
                    log.error("이미지 처리 중 오류 발생: {}", err.message)
                    try {
                        mediaList.add(
                            Media(
                                MimeTypeUtils.parseMimeType(image.contentType ?: "image/png"),
                                image.resource
                            )
                        )
                    } catch (e: Exception) {
                        log.error("원본 이미지 추가 중 오류 발생: {}", e.message)
                    }
                }
            }
        }

        val userMessage = UserMessage(prompt, mediaList)

        return chatClient.prompt()
            .messages(userMessage)
            .call()
            .entity(ProductAnalysisResult::class.java)

        /* dummy
        return new ProductAnalysisResult(
                productName,
                userDescription,
                images.getFirst().getName()
        );*/
    }

    @Throws(IOException::class)
    private fun resizeImage(originalImage: MultipartFile, targetSize: Int, format: String): ByteArray {
        val image = ImageIO.read(originalImage.inputStream) ?: return originalImage.bytes // 이미지가 아니면 원본 반환

        val originalWidth = image.width
        val originalHeight = image.height

        // 이미지가 targetSize보다 작으면 리사이징 안 함
        if (originalWidth <= targetSize && originalHeight <= targetSize) {
            // 포맷 통일을 위해 변환은 수행 (선택사항)
            return ByteArrayOutputStream().use {
                ImageIO.write(image, format, it)
                it.toByteArray()
            }
        }

        // 비율 유지하며 새 크기 계산
        val newWidth: Int
        val newHeight: Int
        if (originalWidth > originalHeight) {
            newWidth = targetSize
            newHeight = (originalHeight * (targetSize.toDouble() / originalWidth)).toInt()
        } else {
            newHeight = targetSize
            newWidth = (originalWidth * (targetSize.toDouble() / originalHeight)).toInt()
        }

        // 리사이징 실행
        val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.createGraphics()
        graphics.drawImage(image, 0, 0, newWidth, newHeight, null)
        graphics.dispose()

        // 바이트 배열로 변환
        return ByteArrayOutputStream().use {
            ImageIO.write(resizedImage, format, it)
            it.toByteArray()
        }
    }
}
