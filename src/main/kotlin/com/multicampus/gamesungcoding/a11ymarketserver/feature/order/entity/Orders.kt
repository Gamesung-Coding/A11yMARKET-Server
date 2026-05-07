package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "ORDERS")
class Orders(
    @Column(length = 16, nullable = false, updatable = false)
    var userId: UUID,

    @Column(length = 30, nullable = false)
    var userName: String,

    @Column(length = 150, nullable = false)
    var userEmail: String,

    @Column(length = 15, nullable = false)
    var userPhone: String?,

    @Column(length = 30, nullable = false)
    var receiverName: String,

    @Column(length = 15, nullable = false)
    var receiverPhone: String,

    @Column(length = 5, nullable = false)
    var receiverZipcode: String,

    @Column(length = 100, nullable = false)
    var receiverAddr1: String,

    @Column(length = 200)
    var receiverAddr2: String?,

    @Column(nullable = false)
    var totalPrice: Int
) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    var orderId: UUID? = null

    @Column(length = 200)
    var paymentKey: String? = null

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null

    @OneToMany(mappedBy = "order")
    var orderItems: MutableList<OrderItems> = mutableListOf()

    fun updateTotalPrice(totalPrice: Int) {
        this.totalPrice = totalPrice
    }

    @Deprecated("미구현")
    fun updateOrderItemStatus() {
        // this.orderStatus = orderStatus
    }

    fun updatePaymentKey(paymentKey: String?) {
        this.paymentKey = paymentKey
    }
}