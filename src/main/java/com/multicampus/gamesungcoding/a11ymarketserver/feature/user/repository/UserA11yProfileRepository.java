package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserA11yProfileRepository extends JpaRepository<UserA11yProfile, UUID> {

    // 특정 사용자 접근성 프로필 목록 조회
    List<UserA11yProfile> findAllByUserIdOrderByUpdatedAtDesc(UUID userId);

    // 사용자 프로필 단건 조회
    Optional<UserA11yProfile> findByProfileIdAndUserId(UUID profileId, UUID userId);

    // 프로필 삭제
    Long deleteByProfileIdAndUserId(UUID profileId, UUID userId);
    
}
