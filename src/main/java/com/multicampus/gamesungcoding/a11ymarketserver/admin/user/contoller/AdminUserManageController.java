package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.contoller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service.AdminUserManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminUserManageController {
    private final AdminUserManageService userService;

    @GetMapping("/v1/admin/users")
    public List<String> inquireUsers(@RequestParam String userId) {
        log.info("inquireUsers userId={}", userId);

        return userService.listAll().stream().map(User::getUserName).toList();
    }
}
