package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "ORDERS")
class Orders(
    userName: String,
    userEmail: String,
    userPhone: String,
    receiverName: String,
    receiverPhone: String,
    receiverZipcode: String?,
    receiverAddr1: String,
    receiverAddr2: String?,
    totalPrice: Int
) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    var orderId: UUID? = null
        private set

    @Column(length = 30, nullable = false)
    var userName: String = userName
        private set

    @Column(length = 150, nullable = false)
    var userEmail: String = userEmail
        private set

    @Column(length = 15, nullable = false)
    var userPhone: String = userPhone
        private set

    @Column(length = 30, nullable = false)
    var receiverName: String = receiverName
        private set

    @Column(length = 15, nullable = false)
    var receiverPhone: String = receiverPhone
        private set

    @Column(length = 5, columnDefinition = "CHAR(5)")
    var receiverZipcode: String? = receiverZipcode
        private set

    @Column(length = 100, nullable = false)
    var receiverAddr1: String = receiverAddr1
        private set

    @Column(length = 200)
    var receiverAddr2: String? = receiverAddr2
        private set

    @Column(nullable = false)
    var totalPrice: Int = totalPrice
        private set

    @Column(length = 200)
    var paymentKey: String? = null
        private set

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        private set

    @OneToMany(mappedBy = "order")
    var orderItems: MutableList<OrderItems> = mutableListOf()
        private set

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