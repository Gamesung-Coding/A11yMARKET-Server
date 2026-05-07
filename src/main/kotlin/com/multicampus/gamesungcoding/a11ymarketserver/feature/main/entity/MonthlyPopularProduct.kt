package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.util.*

@Entity
@Immutable
@Table(name = "view_monthly_popular_products")
class MonthlyPopularProduct(
    @Column
    private var productName: String,

    @Column
    private var productPrice: Int,

    @Column
    private var productImageUrl: String,

    @Column
    private var categoryId: UUID,

    @Column
    private var categoryName: String,

    @Column
    private var sellerId: UUID,

    @Column
    private var monthlySalesVolume: Long,

    @Column
    private var monthlyOrderCount: Long,

    @Column
    private var ranking: Int,

    ) {
    @Id
    @Column
    var productId: UUID? = null
}
