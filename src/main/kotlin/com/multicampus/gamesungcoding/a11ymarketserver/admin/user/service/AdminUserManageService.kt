package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserAdminResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper.toAdminResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AdminUserManageService(
    private val userRepository: UserRepository
) {
    fun listAll(): List<UserAdminResponse> =
        userRepository.findAll().map { it.toAdminResponse() }

    @Transactional
    fun changePermission(userId: UUID, role: UserRole): UserResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found with id: $userId")

        user.changeRole(role)
        return user.toResponse()
    }
}
