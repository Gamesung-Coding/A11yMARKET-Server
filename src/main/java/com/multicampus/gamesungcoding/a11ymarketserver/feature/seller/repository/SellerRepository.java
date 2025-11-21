package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {

    Optional<Seller> findByUserId(UUID userId);

    @Query("""
            SELECT s FROM Seller s
                    WHERE s.userId = (
                        SELECT u.userId FROM Users u
                        WHERE u.userEmail = :email
                    )
            """)
    Optional<Seller> findByUserEmail(@Param("email") String userEmail);
}
