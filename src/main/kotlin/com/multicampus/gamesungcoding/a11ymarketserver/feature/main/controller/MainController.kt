package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CategoryRecommendResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.EventResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service.MainService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/main/")
class MainController(
    private val mainService: MainService
) {

    @GetMapping("products/populars")
    fun popularProducts(): List<MonthlyPopularProduct> = mainService.findTop10ByOrderByRankingAsc()


    @GetMapping("products/categories")
    fun categories(): List<CategoryRecommendResponse> = mainService.getAllCategories()


    @GetMapping("events")
    fun events(): List<EventResponse> = mainService.getAllEvents()
}
