package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model

data class AdminOrderSearchRequest(
    val searchType: String = "",
    val keyword: String = "",
    val startDate: String = "",
    val endDate: String = ""
)
