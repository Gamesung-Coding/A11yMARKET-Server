package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.TossPaymentProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.*

@Service
class TossPaymentService(
    private val tossPaymentProperties: TossPaymentProperties
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val restClient: RestClient = run {
        val encodedKey = Base64.getEncoder()
            .encodeToString("${tossPaymentProperties.secretKey}:".toByteArray())

        RestClient.builder()
            .baseUrl("https://api.tosspayments.com/v1/payments")
            .defaultHeader("Authorization", "Basic $encodedKey")
            .defaultHeader("Content-Type", "application/json")
            .build()
    }

    fun confirmPayment(paymentKey: String, orderId: String, amount: Int) {
        val body = mapOf(
            "paymentKey" to paymentKey,
            "orderId" to orderId,
            "amount" to amount
        )

        try {
            restClient.post()
                .uri("/confirm")
                .body(body)
                .retrieve()
                .toBodilessEntity()
        } catch (e: Exception) {
            log.error("Payment confirmation failed for paymentKey: {}. Error: {}", paymentKey, e.message)
            throw InvalidRequestException("결제 승인에 실패했습니다.")
        }
    }

    fun cancelPayment(paymentKey: String, reason: String, cancelAmount: Int) {
        val body = mapOf(
            "cancelReason" to reason,
            "cancelAmount" to cancelAmount
        )

        try {
            restClient.post()
                .uri("/{paymentKey}/cancel", paymentKey)
                .body(body)
                .retrieve()
                .toBodilessEntity()
        } catch (e: Exception) {
            log.error("Payment cancellation failed for paymentKey: {}. Error: {}", paymentKey, e.message)
            throw InvalidRequestException("결제 취소에 실패했습니다.")
        }
    }
}
