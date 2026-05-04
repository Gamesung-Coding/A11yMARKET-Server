package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.util.*

@Entity
class RefreshToken(
    user: Users,
    token: String,
    expiryDate: LocalDateTime
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    var refreshTokenId: UUID? = null
        private set

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: Users = user
        private set

    @Column(nullable = false, unique = true)
    var token: String = token
        private set

    @Column(nullable = false)
    var expiryDate: LocalDateTime = expiryDate
        private set

    fun updateToken(token: String, expiryDate: LocalDateTime) {
        this.token = token
        this.expiryDate = expiryDate
    }
}
