package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.mapper.toDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper.toDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toCartItemDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

// 결제 정보 조회
@Service
@Transactional(readOnly = true)
class OrderService(
    private val cartItemRepository: CartItemRepository,
    private val addressRepository: AddressRepository,
    private val ordersRepository: OrdersRepository,
    private val orderItemsRepository: OrderItemsRepository,
    private val productRepository: ProductRepository,
    private val tossPaymentService: TossPaymentService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun getOrderSheet(req: OrderSheetRequest): OrderSheetResponse {
        val orderItems: List<CartItemDto> = if (!req.cartItemIds.isNullOrEmpty()) {
            val itemIds = req.cartItemIds.map { UUID.fromString(it) }
            val cartItems = cartItemRepository.findAllById(itemIds)

            cartItems.map { item ->
                val product = requireNotNull(item.product) { "장바구니 상품 정보가 존재하지 않습니다." }

                if (product.productStock < item.quantity) {
                    throw InvalidRequestException("재고가 부족한 상품이 포함되어 있습니다.")
                }

                if (product.productStatus != ProductStatus.APPROVED) {
                    throw InvalidRequestException("구매할 수 없는 상품이 포함되어 있습니다.")
                }

                item.toDto()
            }
        } else {
            val orderItemReq = req.directOrderItem
                ?: throw InvalidRequestException("직접 주문 정보가 없습니다.")

            val product = productRepository.findByIdOrNull(UUID.fromString(orderItemReq.productId))
                ?: throw DataNotFoundException("상품을 찾을 수 없습니다.")

            if (product.productStatus != ProductStatus.APPROVED) {
                throw InvalidRequestException("구매할 수 없는 상품입니다.")
            }

            if (product.productStock < orderItemReq.quantity) {
                throw InvalidRequestException("재고가 부족한 상품입니다.")
            }

            listOf(product.toCartItemDTO(orderItemReq.quantity))
        }

        val totalAmount = orderItems.sumOf { it.productPrice * it.quantity }
        val shippingFee = 0

        return OrderSheetResponse(
            orderItems,
            totalAmount,
            shippingFee,
            totalAmount + shippingFee
        )
    }

    // 주문 생성
    @Transactional
    fun createOrder(userEmail: String, req: OrderCreateRequest): OrderResponse {
        val address = addressRepository.findByAddressIdAndUserUserEmail(
            UUID.fromString(req.addressId),
            userEmail
        ) ?: throw DataNotFoundException("주소를 찾을 수 없습니다.")

        val order = ordersRepository.save(
            Orders(
                userName = address.user.userName,
                userEmail = address.user.userEmail,
                userPhone = address.user.userPhone,
                receiverName = address.address.receiverName,
                receiverPhone = address.address.receiverPhone,
                receiverZipcode = address.address.receiverZipcode,
                receiverAddr1 = address.address.receiverAddr1,
                receiverAddr2 = address.address.receiverAddr2,
                totalPrice = 0
            )
        )
        var totalAmount = 0

        val orderItemsList: List<OrderItems> = if (!req.cartItemIds.isNullOrEmpty()) {
            requireNotNull(req.cartItemIds) { "장바구니에 상품이 없습니다." }

            val cartItems = this.getCartItemsByIds(userEmail, req.cartItemIds)

            cartItems.map { cartItem ->
                val product = requireNotNull(cartItem.product) { "장바구니의 상품정보를 찾을 수 없습니다." }
                validateProduct(product, cartItem.quantity)

                val item = createOrderItemFromProduct(order, product, cartItem.quantity)
                totalAmount += item.productPrice * item.productQuantity
                item
            }
        } else if (req.directOrderItem != null) {
            val directItemReq = req.directOrderItem

            val product = productRepository.findByIdOrNull(UUID.fromString(directItemReq.productId))
                ?: throw DataNotFoundException("상품을 찾을 수 없습니다.")

            validateProduct(product, directItemReq.quantity)

            val item = createOrderItemFromProduct(order, product, directItemReq.quantity)
            totalAmount += item.productPrice * item.productQuantity
            listOf(item)
        } else {
            throw InvalidRequestException("주문할 아이템이 없습니다.")
        }

        orderItemsRepository.saveAll(orderItemsList)
        order.updateTotalPrice(totalAmount)

        return order.toResponse()
    }

    // 내 주문 목록 조회
    @Transactional(readOnly = true)
    fun getMyOrders(userEmail: String): List<OrderResponse> {
        return ordersRepository.findAllByUserEmailOrderByCreatedAtDesc(userEmail)
            .map { it.toResponse() }
    }

    // 내 주문 상세 조회
    @Transactional(readOnly = true)
    fun getMyOrderDetail(orderItemId: UUID, userEmail: String): OrderDetailResponse {
        val orderItem = orderItemsRepository.findByIdOrNull(orderItemId)
            ?: throw DataNotFoundException("주문 상품을 찾을 수 없습니다.")

        if (orderItem.order.userEmail != userEmail) {
            throw InvalidRequestException("해당 주문 상품에 대한 권한이 없습니다.")
        }

        return orderItem.toDetailResponse()
    }

    @Transactional
    fun cancelOrderItems(userEmail: String, req: OrderCancelRequest) {
        // 권한 검증
        val orderItem = orderItemsRepository.findByIdOrNull(UUID.fromString(req.orderItemId))
            ?: throw DataNotFoundException("주문 상품을 찾을 수 없습니다.")

        val order: Orders = ordersRepository.findByOrderIdAndUserEmail(orderItem.order.orderId!!, userEmail)
            ?: throw InvalidRequestException("해당 주문 상품에 대한 권한이 없습니다.")

        when (orderItem.orderItemStatus) {
            OrderItemStatus.ORDERED,
            OrderItemStatus.PAID -> {

                tossPaymentService.cancelPayment(
                    requireNotNull(order.paymentKey) { "결제 키를 찾을 수 없습니다." },
                    req.reason,
                    orderItem.productPrice * orderItem.productQuantity
                )

                orderItem.cancelOrderItem(req.reason)
                orderItem.product.fillUpStock(orderItem.productQuantity)
            }

            OrderItemStatus.ACCEPTED, OrderItemStatus.SHIPPED -> orderItem.cancelOrderItem(req.reason)
            else -> throw InvalidRequestException("취소할 수 없는 주문 상태입니다.")
        }
    }

    // 주문 구매 확정
    @Transactional
    fun confirmOrderItems(userEmail: String, req: OrderConfirmRequest) {
        val itemUuid = runCatching { UUID.fromString(req.orderItemId) }
            .getOrElse { throw InvalidRequestException("유효하지 않은 주문 상품 ID가 포함되어 있습니다.") }

        val item = orderItemsRepository.findByIdOrNull(itemUuid)
            ?: throw DataNotFoundException("주문 상품을 찾을 수 없습니다.")

        if (item.order.userEmail != userEmail) {
            throw InvalidRequestException("해당 주문 상품에 대한 권한이 없습니다.")
        }


        if (item.orderItemStatus == OrderItemStatus.CONFIRMED) {
            throw InvalidRequestException("이미 구매 확정된 상품이 포함되어 있습니다.")
        }

        if (item.orderItemStatus != OrderItemStatus.SHIPPED) {
            throw InvalidRequestException("배송 완료된 상품만 구매 확정할 수 있습니다.")
        }

        item.updateOrderItemStatus(OrderItemStatus.CONFIRMED)
    }

    // 결제 검증
    @Transactional
    fun verifyPayment(userEmail: String, req: PaymentVerifyRequest): PaymentVerifyResponse {
        val orderUuid = runCatching { UUID.fromString(req.orderId) }
            .getOrElse { throw InvalidRequestException("유효하지 않은 주문 상품 ID가 포함되어 있습니다.") }

        val order = ordersRepository.findByOrderIdAndUserEmail(orderUuid, userEmail)
            ?: throw DataNotFoundException("주문을 찾을 수 없습니다.")

        val items = orderItemsRepository.findAllByOrderOrderId(order.orderId!!)

        if (items.isEmpty()) {
            throw InvalidRequestException("주문 상품이 없습니다.")
        }

        val expectedAmount = items
            .filter { it.orderItemStatus == OrderItemStatus.ORDERED }
            .sumOf { it.productPrice * it.productQuantity }

        if (expectedAmount != req.amount) {
            throw InvalidRequestException("결제 금액이 일치하지 않습니다.")
        }

        items.forEach { item ->
            if (item.orderItemStatus != OrderItemStatus.ORDERED) {
                throw InvalidRequestException("결제할 수 없는 상품이 포함되어 있습니다.")
            }
            item.updateOrderItemStatus(OrderItemStatus.PAID)
            // 재고 차감
            item.product.fillUpStock(-item.productQuantity)
        }

        requireNotNull(req.paymentKey) { "결제 키를 찾을 수 없습니다." }
        tossPaymentService.confirmPayment(req.paymentKey, req.orderId, req.amount)
        // 주문 paymentKey 저장
        order.updatePaymentKey(req.paymentKey)

        if (!req.cartItemIdsToDelete.isNullOrEmpty()) {
            val cartUuids = req.cartItemIdsToDelete.map {
                runCatching { UUID.fromString(it) }
                    .getOrElse { throw InvalidRequestException("유효하지 않은 주문 상품 ID가 포함되어 있습니다.") }
            }
            cartItemRepository.deleteAllByIdInBatch(cartUuids)
        }

        return PaymentVerifyResponse(
            orderId = order.orderId!!,
            status = "PAID",
            amount = expectedAmount,
            paidAt = LocalDateTime.now()
        )
    }

    // Helper methods
    private fun getCartItemsByIds(userEmail: String, orderItemIds: List<String>): List<CartItems> {
        val itemUuids = runCatching { orderItemIds.map { UUID.fromString(it) } }
            .getOrElse { throw InvalidRequestException("유효하지 않은 장바구니 아이템 ID가 포함되어 있습니다.") }


        val cartItems: List<CartItems> = cartItemRepository.findAllByIdWithProductAndImage(itemUuids)
        if (cartItems.size != itemUuids.size) {
            log.debug("Requested IDs: {}, Found IDs: {}", itemUuids, cartItems.map { it.cartItemId })
            throw InvalidRequestException("일부 장바구니 상품을 찾을 수 없습니다.")
        }

        // 소유자 검증
        val emailList = cartItems
            .map { it.cart.user.userEmail }
            .distinct()

        // 소유자가 요청자와 일치하는지 확인
        if (emailList.size != 1 || emailList.first() != userEmail) {
            throw InvalidRequestException("장바구니 아이템의 소유자와 요청자가 일치하지 않습니다.")
        }

        return cartItems
    }

    private fun validateProduct(product: Product, quantity: Int) {
        if (product.productStatus != ProductStatus.APPROVED) {
            throw InvalidRequestException("구매할 수 없는 상품이 포함되어 있습니다.")
        }

        if (product.productStock < quantity) {
            throw InvalidRequestException("재고가 부족한 상품이 포함되어 있습니다.")
        }
    }

    private fun createOrderItemFromProduct(order: Orders, product: Product, quantity: Int): OrderItems {
        val imageUrl = product.productImages.firstOrNull()?.imageUrl

        return OrderItems(
            order,
            product,
            product.productName,
            product.productPrice,
            quantity,
            imageUrl
        )

    }
}
