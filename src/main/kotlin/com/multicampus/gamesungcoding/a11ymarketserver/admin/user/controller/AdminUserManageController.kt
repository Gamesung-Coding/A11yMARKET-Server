package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.controller

import com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service.AdminUserManageService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserAdminResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class AdminUserManageController(
    private val userService: AdminUserManageService
) {
    // 관리자 - 전체 사용자 조회
    @GetMapping("/v1/admin/users")
    fun inquireUsers(): List<UserAdminResponse> =
        userService.listAll()

    // 관리자 - 사용자 권한 변경
    @PatchMapping("/v1/admin/users/{userId}/permission")
    fun changeUserPermission(
        @PathVariable userId: UUID,
        @RequestParam role: UserRole
    ): UserResponse = userService.changePermission(userId, role)
}
