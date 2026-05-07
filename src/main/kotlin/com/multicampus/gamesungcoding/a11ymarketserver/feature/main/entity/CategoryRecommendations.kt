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
    var rootCategoryId: UUID?,

    @Column(name = "root_name")
    var rootCategoryName: String?,

    @Column
    var productName: String,

    @Column
    var productPrice: Int,

    @Column
    var productImageUrl: String,

    @Column
    var monthlySalesVolume: Long,
) {
    @Id
    @Column(columnDefinition = "RAW(16)")
    var productId: UUID? = null
}
