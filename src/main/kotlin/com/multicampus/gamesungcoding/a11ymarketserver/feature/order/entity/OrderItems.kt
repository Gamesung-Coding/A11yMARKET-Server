package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(name = "ORDER_ITEMS")
class OrderItems(
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "order_id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var order: Orders,

    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var product: Product,

    @Column(nullable = false)
    var productName: String,

    @Column(nullable = false)
    var productPrice: Int,

    @Column(nullable = false)
    var productQuantity: Int,

    @Column
    var productImageUrl: String? = null,

    ) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    var orderItemId: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    var orderItemStatus: OrderItemStatus = OrderItemStatus.ORDERED

    @Lob
    @Column(columnDefinition = "TEXT")
    var cancelReason: String? = null

    fun cancelOrderItem(reason: String?) {
        when (this.orderItemStatus) {
            OrderItemStatus.CONFIRMED ->
                throw IllegalStateException("이미 구매를 확정한 상품은 취소할 수 없습니다.")

            OrderItemStatus.REJECTED ->
                throw IllegalStateException("이미 거부된 상품은 취소할 수 없습니다.")

            OrderItemStatus.CANCEL_PENDING,
            OrderItemStatus.CANCELED,
            OrderItemStatus.RETURN_PENDING,
            OrderItemStatus.RETURNED ->
                throw IllegalStateException("이미 취소 요청이 진행 중이거나 완료된 상품입니다.")

            OrderItemStatus.CANCEL_REJECTED ->
                throw IllegalStateException("취소 요청이 거부된 상품입니다.")

            OrderItemStatus.RETURN_REJECTED ->
                throw IllegalStateException("반품 요청이 거부된 상품입니다.")

            OrderItemStatus.ACCEPTED,
            OrderItemStatus.SHIPPING,
            OrderItemStatus.SHIPPED -> {
                this.orderItemStatus = OrderItemStatus.CANCEL_PENDING
                this.cancelReason = reason
                return
            }

            else -> {
                this.orderItemStatus = OrderItemStatus.CANCELED
                this.cancelReason = reason
            }
        }
    }

    fun updateOrderItemStatus(status: OrderItemStatus) {
        this.orderItemStatus = status
    }
}
