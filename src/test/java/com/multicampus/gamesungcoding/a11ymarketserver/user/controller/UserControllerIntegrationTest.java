package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.AddressInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
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
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ProductRepository productRepository;

    private final String mockUserEmail = "user1@example.com";
    private final String mockSellerEmail = "seller@example.com";

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
        this.cartRepository.save(
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
    @DisplayName("회원 탈퇴 통합 테스트: 일반 유저 - 성공 케이스")
    @WithMockUser(username = mockUserEmail, roles = "USER")
    void deleteUser_AsNormalUser_Success() throws Exception {
        var deleteRequest = new UserDeleteRequest(
                "password123!"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // 삭제 여부 검증
        var deletedUser = this.userRepository.findByUserEmail(this.mockUser.getUserEmail());
        Assertions.assertThat(deletedUser).isEmpty();
    }

    @Test
    @DisplayName("회원 탈퇴 통합 테스트: 일반 유저 - 실패 케이스 (잘못된 비밀번호)")
    @WithMockUser(username = mockUserEmail, roles = "USER")
    void deleteUser_AsNormalUser_Fail_WrongPassword() throws Exception {
        var deleteRequest = new UserDeleteRequest(
                "wrongpassword!"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 삭제 여부 검증
        var existingUser = this.userRepository.findByUserEmail(this.mockUser.getUserEmail());
        Assertions.assertThat(existingUser).isPresent();
    }

    @Test
    @DisplayName("회원 탈퇴 통합 테스트: 판매자 유저 - 성공 케이스")
    @WithMockUser(username = mockSellerEmail, roles = "SELLER")
    void deleteUser_AsSeller_Success() throws Exception {
        var deleteRequest = new UserDeleteRequest(
                "sellerpass!"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // 삭제 여부 검증
        var deletedUser = this.userRepository.findByUserEmail(this.mockSeller.getUserEmail());
        Assertions.assertThat(deletedUser).isEmpty();

        // 판매자 정보도 삭제되었는지 확인
        var deletedSeller = this.sellerRepository.findByUser_UserEmail(this.mockSeller.getUserEmail());
        Assertions.assertThat(deletedSeller).isEmpty();

        // 상품 정보는 논리 삭제 되었는지 확인
        var products = this.productRepository.findAllBySeller_User_UserEmail(this.mockSeller.getUserEmail());
        for (var product : products) {
            Assertions.assertThat(product.getProductStatus()).isEqualTo(ProductStatus.DELETED);
        }
    }

    @Test
    @DisplayName("회원 탈퇴 통합 테스트: 판매자 유저 - 실패 케이스 (진행 중인 주문 존재)")
    @WithMockUser(username = mockSellerEmail, roles = "SELLER")
    void deleteUser_AsSeller_Fail_OngoingOrders() throws Exception {
        // 주문 항목 생성 (진행 중인 주문 상태로 설정)
        var orderItem = OrderItems.builder()
                .order(this.mockOrder)
                .product(this.mockProduct)
                .productName("Test Product")
                .productQuantity(2)
                .productPrice(25000)
                .build();
        orderItem.updateOrderItemStatus(OrderItemStatus.PAID);
        this.orderItemsRepository.save(orderItem);

        var deleteRequest = new UserDeleteRequest(
                "sellerpass!"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
