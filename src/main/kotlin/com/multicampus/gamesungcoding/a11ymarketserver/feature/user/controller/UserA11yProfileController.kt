package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileReq
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserA11yProfileService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/me/a11y")
class UserA11yProfileController(
    private val profileService: UserA11yProfileService
) {

    // 접근성 프로필 목록 조회
    @GetMapping("/profiles")
    fun getMyProfiles(
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<UserA11yProfileResponse> =
        profileService.getMyProfiles(UUID.fromString(userDetails.username))


    // 접근성 프로필 생성
    @PostMapping("/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody req: UserA11yProfileReq,
        response: HttpServletResponse
    ): UserA11yProfileResponse {
        val saved = profileService.createProfile(UUID.fromString(userDetails.username), req)
        response.setHeader("Location", "/api/v1/users/me/a11y/profiles/${saved.profileId}")
        return saved
    }


    // 접근성 프로필 전체 수정
    @PutMapping("/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable profileId: String,
        @Valid @RequestBody req: @Valid UserA11yProfileReq
    ) = runCatching {
        val id: UUID = UUID.fromString(profileId)
        profileService.updateProfile(UUID.fromString(userDetails.username), id, req)
    }.getOrElse { throw InvalidRequestException("잘못된 UUID 형식입니다.") }

    // 접근성 프로필 삭제
    @DeleteMapping("/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable profileId: String
    ) = runCatching {
        val id: UUID = UUID.fromString(profileId)
        profileService.deleteProfile(UUID.fromString(userDetails.username), id)
    }.getOrElse { throw InvalidRequestException("잘못된 UUID 형식입니다.") }
}