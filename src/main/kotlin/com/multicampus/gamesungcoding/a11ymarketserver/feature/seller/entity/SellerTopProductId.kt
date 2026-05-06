package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
class SellerTopProductId : Serializable {
    @Column
    var sellerId: UUID? = null

    @Column
    var productId: UUID? = null
}
