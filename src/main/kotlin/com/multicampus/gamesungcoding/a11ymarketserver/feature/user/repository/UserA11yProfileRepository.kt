package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserA11yProfile
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserA11yProfileRepository : JpaRepository<UserA11yProfile, UUID> {
    // 특정 사용자 접근성 프로필 목록 조회
    fun findAllByUserUserIdOrderByCreatedAtAsc(userId: UUID): List<UserA11yProfile>

    fun findByProfileIdAndUserUserId(profileId: UUID, userId: UUID): UserA11yProfile?

    // 프로필 삭제
    fun deleteByProfileIdAndUserUserId(profileId: UUID, userId: UUID): Long

    // 사용자별 프로필 이름 중복 체크
    fun existsByUserUserIdAndProfileInfoProfileName(userId: UUID, profileName: String): Boolean
}