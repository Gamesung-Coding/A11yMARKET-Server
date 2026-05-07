package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "product_ai_summary")
@EntityListeners(AuditingEntityListener::class)
class ProductAiSummary(
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var product: Product,

    @Lob
    @Column(columnDefinition = "TEXT")
    var summaryText: String? = null,

    @Lob
    @Column(columnDefinition = "TEXT")
    var usageContext: String? = null,

    @Lob
    @Column(columnDefinition = "TEXT")
    var usageMethod: String? = null
) {
    @Id
    @Column
    var productId: UUID? = null

    @CreatedDate
    @Column(updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var generatedAt: LocalDateTime? = null

    fun updateSummary(summaryText: String?, usageContext: String?, usageMethod: String?) {
        this.summaryText = summaryText
        this.usageContext = usageContext
        this.usageMethod = usageMethod
    }
}
