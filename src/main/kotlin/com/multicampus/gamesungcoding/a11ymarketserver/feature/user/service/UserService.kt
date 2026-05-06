package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserPWUpdateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserOauthLinksRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,

    private val authService: AuthService,
    private val orderItemsRepository: OrderItemsRepository,
    private val productRepository: ProductRepository,
    private val sellerService: SellerService,
    private val userOauthLinksRepository: UserOauthLinksRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    // 마이페이지 - 회원 정보 조회
    fun getUserInfo(userId: String): UserResponse =
        getUserById(userId).toResponse()


    // 마이페이지 - 회원 정보 수정
    @Transactional
    fun updateUserInfo(userId: String, dto: UserUpdateRequest): UserResponse {
        val user = getUserById(userId)
        user.updateUserInfo(dto)
        return user.toResponse()
    }

    @Transactional
    fun updateUserPassword(userId: String, dto: UserPWUpdateRequest) {
        val user = getUserById(userId)
        val currentPassword = user.userPass
            ?: throw InvalidRequestException("현재 비밀번호가 설정되어 있지 않습니다.")

        if (!passwordEncoder.matches(dto.currentPassword, currentPassword)) {
            throw InvalidRequestException("현재 비밀번호가 일치하지 않습니다.")
        }

        user.updatePassword(passwordEncoder.encode(dto.newPassword))
    }

    @Transactional
    fun deleteUser(userId: String, req: UserDeleteRequest) {
        val user = getUserById(userId)
        val isOauthUser = userOauthLinksRepository.existsByUser(user)

        val isPasswordMatch = user.userPass?.let {
            passwordEncoder.matches(req.userPassword, it)
        } ?: false

        if (!isPasswordMatch && !isOauthUser) {
            throw InvalidRequestException("비밀번호가 일치하지 않습니다.")
        }

        val userUuid = user.userId ?: throw UserNotFoundException("User not found")

        if (user.userRole == UserRole.SELLER) {
            // 판매자 회원의 경우 진행 중인 주문이 있는지 확인
            if (orderItemsRepository.existsByProductSellerUserUserIdAndOrderItemStatusIn(
                    userUuid,
                    OrderItemStatus.inProgressStatuses
                )
            ) {
                throw InvalidRequestException("진행 중인 주문이 있어 회원 탈퇴가 불가능합니다.")
            }

            // 모든 Product를 논리적으로 삭제 처리
            sellerService.deleteProducts(
                userUuid,
                productRepository.findAllBySellerUserUserId(userUuid)
            )
        }

        log.info("Deleting user with id: {}", userUuid)
        authService.logout(userUuid)
        userRepository.delete(user)
    }

    private fun getUserById(userId: String): Users =
        runCatching {
            val userId = UUID.fromString(userId)
            userRepository.findByIdOrNull(userId)
                ?: throw UserNotFoundException("사용자 정보가 존재하지 않습니다.")
        }.getOrElse {
            throw InvalidRequestException("User ID is invalid: $userId")
        }
}
