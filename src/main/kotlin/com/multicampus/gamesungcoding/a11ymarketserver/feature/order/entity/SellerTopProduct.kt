package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.math.BigDecimal

@Entity
@Immutable
@Table(name = "view_seller_top_products")
class SellerTopProduct {
    @EmbeddedId
    var id: SellerTopProductId? = null

    @Column
    private var productName: String? = null

    @Column
    private var productPrice: BigDecimal? = null

    @Column
    private var productImageUrl: String? = null

    @Column
    private var orderCount: Long? = null

    @Column
    private var totalQuantitySold: Long? = null

    @Column
    private var totalSalesAmount: BigDecimal? = null

    @Column
    private var salesRank: Int? = null
}
