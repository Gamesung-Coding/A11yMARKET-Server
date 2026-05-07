package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MainPageEvents
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface MainPageEventsRepository : JpaRepository<MainPageEvents, UUID> {
    // start_date, end_date 현재 시간 기준으로 이벤트 조회
    fun findAllByStartDateBeforeAndEndDateAfterOrderByEventIdAsc(
        now1: LocalDateTime,
        now2: LocalDateTime
    ): List<MainPageEvents>
}
