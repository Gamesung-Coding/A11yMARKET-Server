package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserOauthLinks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserOauthLinksRepository extends JpaRepository<UserOauthLinks, UUID> {
    Optional<UserOauthLinks> findByOauthProviderId(String providerId);
}
