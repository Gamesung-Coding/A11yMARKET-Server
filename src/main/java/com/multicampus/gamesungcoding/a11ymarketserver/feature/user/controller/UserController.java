package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/v1/users/me")
    public ResponseEntity<UserResponse> getUserInfo(
            @AuthenticationPrincipal Authentication authentication
    ) {
        log.info("UserController getUserInfo");
        UserResponse response = userService.getUserInfo(authentication.getName());

        log.info("User found: {}", response.getUserEmail());
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PatchMapping("/v1/users/me")
    public ResponseEntity<UserResponse> updateUserInfo(
            @AuthenticationPrincipal Authentication authentication,
            //@RequestParam String uuid,
            @Valid @RequestBody UserUpdateRequest request) {

        log.info("UserController updateUserInfo");
        UserResponse response = userService.updateUserInfo(authentication.getName(), request);

        log.info("User updated: {}", response.getUserEmail());
        return ResponseEntity.ok(response);
    }
}
