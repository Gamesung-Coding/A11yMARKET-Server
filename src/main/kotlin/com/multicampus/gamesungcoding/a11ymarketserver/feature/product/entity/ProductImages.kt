package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "product_images")
@EntityListeners(AuditingEntityListener::class)
class ProductImages(
    product: Product,
    imageUrl: String? = null,
    altText: String? = null,
    imageSequence: Int
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    var imageId: UUID? = null
        private set
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var product: Product = product
        private set

    @Column(length = 2048, nullable = false)
    var imageUrl: String? = imageUrl
        private set

    @Lob
    @Column(columnDefinition = "TEXT")
    var altText: String? = altText
        private set

    @Column(nullable = false)
    var imageSequence: Int = imageSequence
        private set

    @CreatedDate
    var createdAt: LocalDateTime? = null
        private set

    fun updateMetadata(metadata: ImageMetadata) {
        this.altText = metadata.altText
        this.imageSequence = metadata.sequence
    }
}
