package com.multicampus.gamesungcoding.a11ymarketserver.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.address.model.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.OrderCreateReqDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.OrderItemReqDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.OrderStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderCreateService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemREpository;

    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UUID createOrder(UUID userId, OrderCreateReqDTO req) {

        // 배송지 검증
        Addresses addr = addressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new IllegalStateException("배송지 정보를 찾을 수 없습니다."));

        if (!addr.getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 배송지가 아닙니다.");
        }

        // 장바구니 확인
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("장바구니가 존재하지 않습니다."));

        List<CartItems> cartItems = cartItemREpository.findByCartId(cart.getCartId())
                .stream().toList();

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        // 실제 요청(orderItems) : 장바구니 아이템 매칭 및 가격 검증
        Map<UUID, OrderItemReqDTO> reqMap = new HashMap<>();
        for (OrderItemReqDTO item : req.getOrderItems()) {
            reqMap.put(item.getProductId(), item);
        }

        long totalPrice = 0L;
        List<OrderItems> orderItemEntities = new ArrayList<>();

        // 각 상품 정보 검증 + OrderItem 생성
        for (CartItems cartItem : cartItems) {
            UUID productId = cartItem.getProductId();

            OrderItemReqDTO itemReq = reqMap.get(productId);
            if (itemReq == null) {
                throw new IllegalStateException("요청한 상품 목록과 장바구니가 일치하지 않습니다.");
            }

            Product p = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalStateException("상품 정보를 찾을 수 없습니다."));

            int quantity = itemReq.getQuantity();
            long subtotal = (long) p.getProductPrice() * quantity;
            totalPrice += subtotal;

            OrderItems oi = OrderItems.builder()
                    .orderItemId(UUID.randomUUID())
                    .orderId(null) //주문 저장 후 세팅
                    .productId(productId)
                    .productName(p.getProductName())
                    .productPrice(p.getProductPrice().longValue())
                    .productQuantity(quantity)
                    .orderItemStatus(OrderItemStatus.PAID) //최초 상태
                    .build();

            orderItemEntities.add(oi);

        }

        // orders 생성
        Orders order = Orders.builder()
                .orderId(UUID.randomUUID())
                .userName("") // user 정보 연동되면 채우기
                .userEmail("")
                .userPhone("")
                .receiverName(addr.getReceiverName())
                .receiverPhone(addr.getReceiverPhone())
                .receiverZipcode(addr.getReceiverZipcode())
                .receiverAddr1(addr.getReceiverAddr1())
                .receiverAddr2(addr.getReceiverAddr2())
                .totalPrice(totalPrice)
                .orderStatus(OrderStatus.PAID_PENDING)
                .build();

        ordersRepository.save(order);

        // OrderItems 저장
        for (OrderItems oi : orderItemEntities) {
            oi.assignOrderId(order.getOrderId());
            orderItemsRepository.save(oi);
        }

        // TO DO : 장바구니 비우기
        //cartItemRepository.deleteByCartId(cart.getCartId());

        return order.getOrderId();

    }

}
