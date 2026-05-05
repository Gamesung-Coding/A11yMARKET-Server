package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import lombok.Data
import java.io.Serializable
import java.util.*

@Embeddable
@Data
class SellerTopProductId : Serializable {
    @Column
    private var sellerId: UUID? = null

    @Column
    var productId: UUID? = null
}
