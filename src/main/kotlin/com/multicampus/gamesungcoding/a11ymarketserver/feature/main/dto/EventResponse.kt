package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MainPageEvents

data class EventResponse(
    val eventTitle: String,
    val eventDescription: String,
    val eventImageUrl: String,
    val eventUrl: String
) {
    companion object {
        fun fromEntity(entity: MainPageEvents): EventResponse {
            return EventResponse(
                entity.eventTitle,
                entity.eventDescription,
                entity.eventImageUrl,
                entity.eventUrl
            )
        }
    }
}
