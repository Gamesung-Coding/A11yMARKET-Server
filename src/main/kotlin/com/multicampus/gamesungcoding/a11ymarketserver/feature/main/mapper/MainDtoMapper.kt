package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CategoryRecommendResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.EventResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MainPageEvents
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories

fun Categories.toRecommendResponse() =
    CategoryRecommendResponse(
        categoryId = this.categoryId ?: throw InvalidRequestException("Category Id not found"),
        categoryName = this.categoryName
    )

fun MainPageEvents.toResponse() =
    EventResponse(
        this.eventTitle,
        this.eventDescription,
        this.eventImageUrl,
        this.eventUrl
    )