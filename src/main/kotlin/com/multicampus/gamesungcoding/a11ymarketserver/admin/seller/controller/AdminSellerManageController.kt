package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.controller

import com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.model.AdminSellerUpdateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.service.AdminSellerService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerApplyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class AdminSellerManageController(
    private val adminSellerService: AdminSellerService
) {
    // 관리자 - 전체 판매자 프로필 조회
    @GetMapping("/v1/admin/sellers")
    fun getAllSellerProfiles(): List<SellerProfileResponse> =
        adminSellerService.getAllSellerProfile()

    // 관리자 - 특정 판매자 프로필 조회
    @GetMapping("/v1/admin/sellers/{sellerId}")
    fun getSellerProfile(@PathVariable sellerId: String): SellerDetailResponse =
        adminSellerService.getSellerProfile(UUID.fromString(sellerId))

    // 관리자 - 판매자 승인 대기 목록 조회
    @GetMapping("/v1/admin/sellers/pending")
    fun inquirePendingSellers(): List<SellerApplyResponse> =
        adminSellerService.inquirePendingSellers()

    // 관리자 - 판매자 상태 변경
    @PatchMapping("/v1/admin/sellers/{sellerId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeSellerStatus(
        @PathVariable sellerId: String,
        @RequestParam status: SellerSubmitStatus
    ) = adminSellerService.updateSellerStatus(UUID.fromString(sellerId), status)

    // 관리자 - 판매자 정보 수정
    @PatchMapping("/v1/admin/sellers/{sellerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateSellerInfo(
        @PathVariable sellerId: String,
        @RequestBody request: AdminSellerUpdateRequest
    ) = adminSellerService.updateSellerInfo(UUID.fromString(sellerId), request)
}
