package com.multicampus.gamesungcoding.a11ymarketserver.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.AddressInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceIntegrationTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product mockProduct;
    private Orders mockOrder;
    private Users mockUser;
    private Users mockSeller;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 저장
        this.mockUser = this.userRepository.save(
                Users.builder()
                        .userEmail("user1@example.com")
                        .userPass(this.passwordEncoder.encode("password123!"))
                        .userName("User One")
                        .userRole(UserRole.USER)
                        .build());
        this.mockSeller = this.userRepository.save(
                Users.builder()
                        .userEmail("seller@example.com")
                        .userPass(this.passwordEncoder.encode("sellerpass!"))
                        .userName("Seller One")
                        .userRole(UserRole.SELLER)
                        .build());

        var seller = Seller.builder()
                .user(this.mockSeller)
                .sellerName(this.mockSeller.getUserName())
                .businessNumber("123-45-67890")
                .sellerIntro("Hello, we are a11y market sellers.")
                .build();
        seller.approve();

        seller = sellerRepository.save(seller);

        // cart 및 address가 있어도 동작하는지 테스트하기 위해 장바구니도 생성
        var mockCart = this.cartRepository.save(
                Cart.builder()
                        .user(this.mockUser)
                        .build()
        );
        var address = this.addressRepository.save(Addresses
                .builder()
                .user(this.mockUser)
                .addressInfo(AddressInfo.builder()
                        .addressName("Home")
                        .receiverName("User One")
                        .receiverPhone("01012345678")
                        .receiverZipcode("12345")
                        .receiverAddr1("123 Main St")
                        .receiverAddr2("Apt 101")
                        .build())
                .isDefault(true)
                .build());

        // 판매자의 상품도 생성
        var category = this.categoryRepository.save(
                Categories.builder()
                        .categoryName("Test Category")
                        .build()
        );
        this.mockProduct = this.productRepository.save(
                Product.builder()
                        .seller(seller)
                        .category(category)
                        .productPrice(25000)
                        .productStock(100)
                        .productName("Test Product")
                        // 판매 승인 상태로 설정
                        .productStatus(ProductStatus.APPROVED)
                        .build());

        // Orders 및 OrderItems도 생성
        this.mockOrder = this.ordersRepository.save(
                Orders.builder()
                        .userName(this.mockUser.getUserName())
                        .userEmail(this.mockUser.getUserEmail())
                        .userPhone("01012345678")
                        .receiverName(address.getAddress().getReceiverName())
                        .receiverPhone(address.getAddress().getReceiverPhone())
                        .receiverZipcode(address.getAddress().getReceiverZipcode())
                        .receiverAddr1(address.getAddress().getReceiverAddr1())
                        .receiverAddr2(address.getAddress().getReceiverAddr2())
                        .totalPrice(50000)
                        // .orderStatus(OrderStatus.PENDING)
                        .build()
        );
    }

    @Test
    @DisplayName("회원 탈퇴: 일반 유저 - 성공")
    void deleteUser_AsRegularUser_Success() {
        var userId = this.mockUser.getUserId();
        var userEmail = this.mockUser.getUserEmail();

        var orderItem = this.orderItemsRepository.save(
                OrderItems.builder()
                        .order(this.mockOrder)
                        .product(this.mockProduct)
                        .productName(this.mockProduct.getProductName())
                        .productPrice(this.mockProduct.getProductPrice())
                        .productQuantity(2)
                        .build());
        orderItem.updateOrderItemStatus(OrderItemStatus.CONFIRMED);

        var req = new UserDeleteRequest("password123!");
        this.userService.deleteUser(this.mockUser.getUserEmail(), req);

        // 사용자, 장바구니, 주소가 모두 삭제되었는지 확인
        Assertions.assertFalse(this.userRepository.findById(userId).isPresent());

        Assertions.assertFalse(this.cartRepository.findByUser_UserId(userId).isPresent());

        Assertions.assertEquals(
                0,
                this.addressRepository.findAllByUser_UserEmail(userEmail).size());

        // 주문 및 주문 상품은 기록이므로, 삭제되지 않아야 함
        Assertions.assertEquals(1, this.ordersRepository.count());
        Assertions.assertEquals(1, this.orderItemsRepository.count());
    }

    @Test
    @DisplayName("회원 탈퇴: 판매자 - 성공")
    void deleteUser_AsSeller_Success() {
        var userId = this.mockSeller.getUserId();
        var userEmail = this.mockSeller.getUserEmail();

        var OrderItem = this.orderItemsRepository.save(
                OrderItems.builder()
                        .order(this.mockOrder)
                        .product(this.mockProduct)
                        .productName(this.mockProduct.getProductName())
                        .productPrice(this.mockProduct.getProductPrice())
                        .productQuantity(2)
                        .build());
        OrderItem.updateOrderItemStatus(OrderItemStatus.CONFIRMED);

        var req = new UserDeleteRequest("sellerpass!");
        this.userService.deleteUser(this.mockSeller.getUserEmail(), req);

        // 사용자, 판매자가 모두 삭제되었는지 확인
        Assertions.assertFalse(this.userRepository.findById(userId).isPresent());
        Assertions.assertFalse(this.sellerRepository.findByUser_UserEmail(userEmail).isPresent());

        // 상품은 삭제되지 않고, 상태가 DELETED로 변경되었는지 확인
        var deletedProduct = this.productRepository.findById(this.mockProduct.getProductId());
        Assertions.assertTrue(deletedProduct.isPresent());
        Assertions.assertEquals(ProductStatus.DELETED, deletedProduct.get().getProductStatus());
    }

    @Test
    @DisplayName("회원 탈퇴: 비밀번호 불일치 - 실패")
    void deleteUser_PasswordMismatch_Failure() {
        var req = new UserDeleteRequest("wrongpassword!");
        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> this.userService.deleteUser(this.mockUser.getUserEmail(), req)
        );
    }

    @Test
    @DisplayName("회원 탈퇴: 판매자 - 상품이 접수 대기 중일 때 실패")
    void deleteUser_SellerWithPendingOrders_Failure() {
        // 판매자의 상품이 접수 대기 중인 상태로 설정
        var pendingOrderItem = this.orderItemsRepository.save(
                OrderItems.builder()
                        .order(this.mockOrder)
                        .product(this.mockProduct)
                        .productName(this.mockProduct.getProductName())
                        .productPrice(this.mockProduct.getProductPrice())
                        .productQuantity(1)
                        .build());
        pendingOrderItem.updateOrderItemStatus(OrderItemStatus.PAID);

        var req = new UserDeleteRequest("sellerpass!");
        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> this.userService.deleteUser(this.mockSeller.getUserEmail(), req)
        );
    }
}
