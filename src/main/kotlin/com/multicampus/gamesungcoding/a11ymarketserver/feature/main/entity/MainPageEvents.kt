package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

@Entity
class MainPageEvents(
    @Column(length = 200, nullable = false)
    var eventTitle: String,

    @Column(length = 1000, nullable = false)
    var eventDescription: String,

    @Column(length = 2048, nullable = false)
    var eventImageUrl: String,

    @Column(length = 2048)
    var eventUrl: String,

    @Column(nullable = false)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var endDate: LocalDateTime
) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private var eventId: UUID? = null
}
