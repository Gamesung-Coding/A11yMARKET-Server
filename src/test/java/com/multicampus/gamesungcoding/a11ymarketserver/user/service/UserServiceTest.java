package com.multicampus.gamesungcoding.a11ymarketserver.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private SellerService sellerService;
    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("회원 탈퇴 성공 - 일반 회원")
    void deleteUserSuccessRegular() {
        String email = "user@example.com";
        String password = "password";

        Users user = Users.builder()
                .userEmail(email)
                .userPass(password)
                .userRole(UserRole.USER)
                .build();

        BDDMockito.when(userRepository.findByUserEmail(email))
                .thenReturn(Optional.of(user));
        BDDMockito.when(passwordEncoder.matches(password, user.getUserPass()))
                .thenReturn(true);

        var req = new UserDeleteRequest(
                password);
        userService.deleteUser(email, req);

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 비밀번호 불일치")
    void deleteUserFailPasswordMismatch() {
        String email = "user@example.com";
        String wrongPass = "wrongPassword";
        Users user = Users.builder()
                .userEmail(email)
                .userPass("password")
                .userRole(UserRole.USER)
                .build();
        BDDMockito.when(userRepository.findByUserEmail(email))
                .thenReturn(Optional.of(user));
        BDDMockito.when(passwordEncoder.matches(wrongPass, user.getUserPass()))
                .thenReturn(false);
        var req = new UserDeleteRequest(
                wrongPass);

        Assertions.assertThrows(InvalidRequestException.class, () -> userService.deleteUser(email, req));
    }

    @Test
    @DisplayName("회원 탈퇴 성공 - 판매자, 진행 중인 주문 없음")
    void deleteUserSuccessSellerNoInProgressOrders() {
        String email = "seller@example.com";
        String password = "password";
        Users user = Users.builder()
                .userEmail(email)
                .userPass(password)
                .userRole(UserRole.SELLER)
                .build();
        BDDMockito.when(userRepository.findByUserEmail(email))
                .thenReturn(Optional.of(user));
        BDDMockito.when(passwordEncoder.matches(password, user.getUserPass()))
                .thenReturn(true);

        var req = new UserDeleteRequest(password);
        userService.deleteUser(email, req);
        Mockito.verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 진행 중인 주문이 있는 판매자")
    void deleteUserFailSellerWithInProgressOrders() {
        String email = "seller@example.com";
        String password = "password";
        Users user = Users.builder()
                .userEmail(email)
                .userPass(password)
                .userRole(UserRole.SELLER)
                .build();
        BDDMockito.when(userRepository.findByUserEmail(email))
                .thenReturn(Optional.of(user));
        BDDMockito.when(passwordEncoder.matches(password, user.getUserPass()))
                .thenReturn(true);
        BDDMockito.when(orderItemsRepository.existsByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
                        user.getUserEmail(),
                        OrderItemStatus.inProgressStatuses()
                ))
                .thenReturn(true);

        var req = new UserDeleteRequest(password);
        Assertions.assertThrows(InvalidRequestException.class, () -> userService.deleteUser(email, req));
    }
}
