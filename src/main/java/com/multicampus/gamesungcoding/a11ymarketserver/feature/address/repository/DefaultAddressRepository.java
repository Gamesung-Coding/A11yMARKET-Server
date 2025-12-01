package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.DefaultAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DefaultAddressRepository extends JpaRepository<DefaultAddress, UUID> {
    // 기본 배송지 조회
    Optional<DefaultAddress> findByUser_UserId(UUID userId);

    DefaultAddress findByUser_UserEmail(@Param("userEmail") String userEmail);
}
