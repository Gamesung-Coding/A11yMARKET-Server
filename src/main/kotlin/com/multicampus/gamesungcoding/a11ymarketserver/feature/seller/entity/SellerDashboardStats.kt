package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.math.BigDecimal
import java.util.*

@Entity
@Immutable
@Table(name = "view_seller_dashboard_stats")
class SellerDashboardStats(
    sellerId: UUID,
    totalRevenue: BigDecimal,
    totalOrderCount: Long,
    confirmedCount: Long,
    refundedCount: Long
) {
    @Id
    @Column
    var sellerId: UUID = sellerId
        private set

    @Column
    var totalRevenue: BigDecimal = totalRevenue
        private set

    @Column
    var totalOrderCount: Long = totalOrderCount
        private set

    @Column
    var confirmedCount: Long = confirmedCount
        private set

    @Column
    var refundedCount: Long = refundedCount
        private set
}
