package com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.dto.AdminDashboardStats;
import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/v1/admin/dashboard/stats")
    public ResponseEntity<AdminDashboardStats> getAdminDashboardStats() {
        return ResponseEntity.ok(adminDashboardService.getAdminDashboardStats());
    }
}
