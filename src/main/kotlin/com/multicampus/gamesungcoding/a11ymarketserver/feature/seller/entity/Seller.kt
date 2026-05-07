package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "sellers")
@EntityListeners(AuditingEntityListener::class)
class Seller(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: Users,

    @Column(nullable = false)
    var sellerName: String,

    @Column(nullable = false)
    var businessNumber: String,

    @Column(length = 1024)
    var sellerIntro: String? = null
) {
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    var sellerId: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var sellerGrade: SellerGrades = SellerGrades.NEWER

    @Column(nullable = false, name = "is_a11y_guarantee")
    var isA11yGuarantee: Boolean = false

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var sellerSubmitStatus: SellerSubmitStatus = SellerSubmitStatus.PENDING

    @CreatedDate
    @Column(updatable = false)
    var submitDate: LocalDateTime? = null

    var approvedDate: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

    @OneToMany(mappedBy = "seller")
    var products: MutableList<Product> = mutableListOf()

    // 비즈니스 로직
    fun approve() {
        this.sellerSubmitStatus = SellerSubmitStatus.APPROVED
        this.approvedDate = LocalDateTime.now()
        this.user.changeRole(UserRole.SELLER)
    }

    fun reject() {
        this.sellerSubmitStatus = SellerSubmitStatus.REJECTED
        this.approvedDate = LocalDateTime.now()
    }

    fun updateAdminSellerInfo(
        sellerName: String?,
        businessNumber: String?,
        sellerIntro: String?,
        sellerGrade: SellerGrades?,
        a11yGuarantee: Boolean?
    ) {
        sellerName?.let { this.sellerName = it }
        businessNumber?.let { this.businessNumber = it }
        sellerIntro?.let { this.sellerIntro = it }
        sellerGrade?.let { this.sellerGrade = it }
        a11yGuarantee?.let { this.isA11yGuarantee = it }
    }

    fun updateSellerInfo(sellerName: String?, sellerIntro: String?) {
        sellerName?.let { this.sellerName = it }
        sellerIntro?.let { this.sellerIntro = it }
    }
}
