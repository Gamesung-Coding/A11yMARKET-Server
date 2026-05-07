package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_oauth_links")
@EntityListeners(AuditingEntityListener::class)
class UserOauthLinks(
    @Column(length = 50)
    var oauthProvider: String,

    @Column
    var oauthProviderId: String,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    var user: Users? = null,
) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    var userOauthLinkId: UUID? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

    fun updateUser(user: Users?) {
        this.user = user
    }
}
