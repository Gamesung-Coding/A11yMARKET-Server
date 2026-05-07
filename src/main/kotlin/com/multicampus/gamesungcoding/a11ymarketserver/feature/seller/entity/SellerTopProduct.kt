package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "view_seller_top_products")
class SellerTopProduct(
    @Column
    var productName: String,

    @Column
    var productPrice: Int,

    @Column
    var productImageUrl: String,
    @Column
    var orderCount: Long,

    @Column
    var totalQuantitySold: Long,

    @Column
    var totalSalesAmount: Int,

    @Column
    var salesRank: Int
) {
    @EmbeddedId
    var id: SellerTopProductId? = null
}
