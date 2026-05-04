package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "USER_A11Y_PROFILES")
@EntityListeners(AuditingEntityListener::class)
class UserA11yProfile(
    profileInfo: A11yProfileInfo,
    user: Users? = null,
) {
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    var profileId: UUID? = null
        private set

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var user: Users? = user
        private set

    @Embedded
    var profileInfo: A11yProfileInfo = profileInfo
        private set

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        private set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null

    fun update(profileInfo: A11yProfileInfo) {
        this.profileInfo = profileInfo
    }
}
