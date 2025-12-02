package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfile;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfileReq;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfileResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserA11yProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserA11yProfileController {

    private final UserA11yProfileService profileService;

    // 접근성 프로필 목록 조회
    @GetMapping("/v1/users/me/a11y/profiles")
    public ResponseEntity<List<UserA11yProfileResponse>> getMyProfiles(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        List<UserA11yProfile> profiles = profileService.getMyProfiles(email);

        return ResponseEntity.ok(
                profiles.stream()
                        .map(UserA11yProfileResponse::fromEntity)
                        .toList()
        );

    }

    // 접근성 프로필 생성
    @PostMapping("/v1/users/me/a11y/profiles")
    public ResponseEntity<UserA11yProfileResponse> createProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserA11yProfileReq req
    ) {
        String email = userDetails.getUsername();
        UserA11yProfile saved = profileService.createProfile(email, req);
        return ResponseEntity
                .created(URI.create("/api/v1/users/me/a11y/profiles/" + saved.getProfileId()))
                .body(UserA11yProfileResponse.fromEntity(saved));
    }


    // 접근성 프로필 전체 수정
    @PutMapping("/v1/users/me/a11y/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String profileId,
            @Valid @RequestBody UserA11yProfileReq req
    ) {
        String email = userDetails.getUsername();

        UUID id;
        try {
            id = UUID.fromString(profileId);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("잘못된 UUID 형식입니다.");
        }
        profileService.updateProfile(email, id, req);
    }

    // 접근성 프로필 삭제
    @DeleteMapping("/v1/users/me/a11y/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String profileId
    ) {
        String email = userDetails.getUsername();

        UUID id;
        try {
            id = UUID.fromString(profileId);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("잘못된 UUID 형식입니다.");
        }
        profileService.deleteProfile(email, id);
    }
}