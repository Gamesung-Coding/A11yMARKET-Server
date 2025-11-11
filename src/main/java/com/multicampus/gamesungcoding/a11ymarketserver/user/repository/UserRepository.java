package com.multicampus.gamesungcoding.a11ymarketserver.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUserEmail(String email);
}
