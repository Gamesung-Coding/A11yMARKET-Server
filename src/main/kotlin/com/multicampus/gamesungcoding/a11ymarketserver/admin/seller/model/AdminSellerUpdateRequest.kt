package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.model

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades

data class AdminSellerUpdateRequest(
    val sellerName: String?,
    val businessNumber: String?,
    val sellerGrade: SellerGrades?,
    val sellerIntro: String?,
    val a11yGuarantee: Boolean?
)
