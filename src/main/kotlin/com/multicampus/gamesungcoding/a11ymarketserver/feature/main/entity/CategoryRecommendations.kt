package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.util.*

@Entity
@Immutable
@Table(name = "view_category_recommendations")
class CategoryRecommendations(
    @Column(name = "root_id", columnDefinition = "RAW(16)")
    private var rootCategoryId: UUID?,

    @Column(name = "root_name")
    private var rootCategoryName: String?,

    @Column
    private var productName: String,

    @Column
    private var productPrice: Int,

    @Column
    private var productImageUrl: String,

    @Column
    private var monthlySalesVolume: Long,
) {
    @Id
    @Column(columnDefinition = "RAW(16)")
    private var productId: UUID? = null
}
