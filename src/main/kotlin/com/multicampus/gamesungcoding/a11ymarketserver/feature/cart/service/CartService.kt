package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.mapper.toDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.mapper.toUpdateResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    private val log = LoggerFactory.getLogger(CartService::class.java)

    @Transactional(readOnly = true)
    fun getCartItems(userId: UUID): CartItemListResponse {
        val cart = getCartByUserId(userId)
        val list = cartItemRepository.findAllByCart(cart).map { it.toDto() }
        val total = list.sumOf { it.quantity * it.productPrice }

        val groupedList = list.groupBy { it.sellerName }
            .map { (sellerName, items) ->
                CartItemListDto(
                    sellerName = sellerName,
                    sellerId = items.first().sellerId,
                    groupTotal = items.sumOf { it.productPrice * it.quantity },
                    items = items
                )
            }

        return CartItemListResponse(groupedList, total)
    }

    @Transactional
    fun addItem(req: CartAddRequest, userId: UUID): CartItemUpdatedResponse {
        val cart = getCartByUserId(userId)
        val productId = UUID.fromString(req.productId)
        val productProxy = productRepository.getReferenceById(productId)

        val cartItem = cartItemRepository.findByCartAndProductProductId(cart, productId)?.apply {
            changeQuantity(this.quantity + req.quantity)
        } ?: CartItems(cart, productProxy, req.quantity)

        return cartItemRepository.save(cartItem).toUpdateResponse()
    }

    @Transactional
    fun updateQuantity(cartItemId: UUID, quantity: Int, userId: UUID): CartItemUpdatedResponse {
        val cart = getCartByUserId(userId)

        val cartItem = cartItemRepository.findByIdOrNull(cartItemId)
            ?: throw DataNotFoundException("Cart item not found: $cartItemId")

        if (cartItem.cart.cartId != cart.cartId) {
            throw DataNotFoundException("Cart item does not belong to user: $cartItemId")
        }

        cartItem.changeQuantity(quantity)

        return cartItemRepository.save(cartItem).toUpdateResponse()
    }

    @Transactional
    fun deleteItems(request: CartItemDeleteRequest, userId: UUID) {
        val itemIds = request.itemIds.map { UUID.fromString(it) }
        val cart = getCartByUserId(userId)

        val invalidItems = cartItemRepository.findAllById(itemIds)
            .filter { it.cart.cartId != cart.cartId }
            .map { it.cartItemId }

        if (invalidItems.isNotEmpty()) {
            throw DataNotFoundException("Some cart items do not belong to user: $invalidItems")
        }

        cartItemRepository.deleteAllByIdInBatch(itemIds)
    }

    private fun getCartByUserId(userId: UUID): Cart {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DataNotFoundException("사용자를 찾을 수 없습니다. userId=$userId")

        // Optional 없이 순수 코틀린 문법으로 처리!
        return cartRepository.findByUser(user) ?: run {
            log.debug("장바구니가 없어 새로 생성합니다. userId={}", userId)
            cartRepository.save(
                Cart(user)
            )
        }
    }

    @Transactional(readOnly = true)
    fun getCartItemCount(userId: UUID): CartItemCountResponse {
        return CartItemCountResponse(
            cartItemRepository.countByCart(getCartByUserId(userId))
        )
    }
}