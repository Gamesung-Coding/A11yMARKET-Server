package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfile;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfileReq;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserA11yProfileRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserA11yProfileService {

    private final UserA11yProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 프로필 목록 조회
    public List<UserA11yProfile> getMyProfiles(String userEmail) {
        UUID userId = getUserIdByEmail(userEmail);
        return profileRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
    }


    // 프로필 생성
    @Transactional
    public UserA11yProfile createProfile(String userEmail, UserA11yProfileReq dto) {
        UUID userId = getUserIdByEmail(userEmail);

        // 프로필 이름 중복 체크
        if (profileRepository.existsByUserIdAndProfileName(userId, dto.profileName())) {
            throw new InvalidRequestException("이미 존재하는 프로필 이름입니다.");
        }

        UserA11yProfile profile = UserA11yProfile.builder()
                .userId(userId)
                .profileName(dto.profileName())
                .description(dto.description())
                .contrastLevel(dto.contrastLevel())
                .textSizeLevel(dto.textSizeLevel())
                .textSpacingLevel(dto.textSpacingLevel())
                .lineHeightLevel(dto.lineHeightLevel())
                .textAlign(dto.textAlign())
                .screenReader(booleanToInt(dto.screenReader()))
                .smartContrast(booleanToInt(dto.smartContrast()))
                .highlightLinks(booleanToInt(dto.highlightLinks()))
                .cursorHighlight(booleanToInt(dto.cursorHighlight()))
                .build();

        return profileRepository.save(profile);
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(String userEmail, UUID profileId, UserA11yProfileReq dto) {
        UUID userId = getUserIdByEmail(userEmail);

        UserA11yProfile profile = profileRepository
                .findByProfileIdAndUserId(profileId, userId)
                .orElseThrow(() -> new DataNotFoundException("해당 접근성 프로필을 찾을 수 없습니다."));

        // 프로필 이름 중복 체크
        if (!profile.getProfileName().equals(dto.profileName())
                && profileRepository.existsByUserIdAndProfileName(userId, dto.profileName())) {
            throw new InvalidRequestException("이미 존재하는 프로필 이름입니다.");
        }

        profile.update(
                dto.profileName(),
                dto.description(),
                dto.contrastLevel(),
                dto.textSizeLevel(),
                dto.textSpacingLevel(),
                dto.lineHeightLevel(),
                dto.textAlign(),
                booleanToInt(dto.screenReader()),
                booleanToInt(dto.smartContrast()),
                booleanToInt(dto.highlightLinks()),
                booleanToInt(dto.cursorHighlight())
        );
    }

    // 프로필 삭제
    @Transactional
    public void deleteProfile(String userEmail, UUID profileId) {
        UUID userId = getUserIdByEmail(userEmail);

        Long deleted = profileRepository.deleteByProfileIdAndUserId(profileId, userId);

        if (deleted == 0) {
            throw new DataNotFoundException("삭제할 수 있는 프로필이 없습니다.");
        }
    }

    // UserEmail -> UserId 변환
    private UUID getUserIdByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."))
                .getUserId();
    }

    private Integer booleanToInt(Boolean value) {
        return (value != null && value) ? 1 : 0;
    }
}
