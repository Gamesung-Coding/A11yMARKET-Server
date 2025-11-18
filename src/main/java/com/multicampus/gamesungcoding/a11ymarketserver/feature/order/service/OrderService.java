package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.DefaultAddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckoutResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// 결제 정보 조회
@Service
@RequiredArgsConstructor
public class OrderService {

    private final DefaultAddressRepository defaultAddressRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public OrderCheckoutResponse getCheckoutInfo(String userEmail, OrderCheckRequest req) {
        var cart = cartRepository.findCartByUserEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("잘못된 요청입니다."));

        // 장바구니 상품 조회
        List<CartItems> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        // 요청된 상품만 필터링
        var reqUuid = req.checkoutItemIds().stream().map(UUID::fromString).toList();
        cartItems.removeIf(item -> !reqUuid.contains(item.getCartItemId()));
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("선택된 상품이 장바구니에 존재하지 않습니다.");
        }

        long totalAmount = 0;

        // 상품별 가격 계산
        for (CartItems item : cartItems) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalStateException("상품 정보를 찾을 수 없습니다."));

            int qty = item.getQuantity();
            long subtotal = p.getProductPrice().longValue() * qty;
            totalAmount += subtotal;
        }

        long shippingFee = 0;
        long finalAmount = totalAmount + shippingFee;

        // 최종 반환
        return new OrderCheckoutResponse(
                totalAmount,
                shippingFee,
                finalAmount
        );
    }


}
