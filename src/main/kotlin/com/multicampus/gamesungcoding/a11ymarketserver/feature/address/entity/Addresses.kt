package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Addresses(
    user: Users,
    address: AddressInfo,
    isDefault: Boolean
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    var addressId: UUID? = null
        private set
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: Users = user
        private set

    @Embedded
    var address: AddressInfo = address
        private set

    @Column(nullable = false)
    var isDefault: Boolean = isDefault
        private set

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        private set

    fun updateAddrInfo(addressInfo: AddressInfo) {
        this.address = addressInfo
    }

    fun setDefault(isDefault: Boolean) {
        this.isDefault = isDefault
    }
}
