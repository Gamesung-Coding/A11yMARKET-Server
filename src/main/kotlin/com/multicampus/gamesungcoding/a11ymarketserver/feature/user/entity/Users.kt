package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class Users(
    userName: String,
    userEmail: String,
    userNickname: String,
    userRole: UserRole,
    userPass: String? = null,
    userPhone: String? = null,
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    var userId: UUID? = null
        private set
    
    @Column(length = 30)
    var userName: String = userName
        private set

    @Column(length = 50)
    var userEmail: String = userEmail
        private set

    @Column(length = 20)
    var userNickname: String = userNickname
        private set

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    var userRole: UserRole = userRole
        private set

    @Column(length = 100)
    var userPass: String? = userPass
        private set

    @Column(length = 15)
    var userPhone: String? = userPhone
        private set

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        private set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
        private set

    @OneToOne(mappedBy = "user")
    var seller: Seller? = null
        private set

    // 회원 정보 수정
    fun updateUserInfo(request: UserUpdateRequest) {
        request.userName?.let { this.userName = it }
        request.userEmail?.let { this.userEmail = it }
        request.userPhone?.let { this.userPhone = it }
        request.userNickname?.let { this.userNickname = it }
    }

    // 비밀번호 변경 메소드
    fun updatePassword(encodedPassword: String?) {
        this.userPass = encodedPassword ?: throw InvalidRequestException("Error while updating password.")
    }

    // 사용자 권한 변경 메소드
    fun changeRole(role: UserRole) {
        this.userRole = role
    }
}
