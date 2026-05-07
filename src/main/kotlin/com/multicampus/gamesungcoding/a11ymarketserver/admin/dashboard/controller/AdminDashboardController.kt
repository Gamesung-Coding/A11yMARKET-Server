package com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.controller

import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.dto.AdminDashboardStats
import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.service.AdminDashboardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AdminDashboardController(
    private val adminDashboardService: AdminDashboardService
) {
    @GetMapping("/v1/admin/dashboard/stats")
    fun getAdminDashboardStats(): AdminDashboardStats =
        adminDashboardService.getAdminDashboardStats()
}
