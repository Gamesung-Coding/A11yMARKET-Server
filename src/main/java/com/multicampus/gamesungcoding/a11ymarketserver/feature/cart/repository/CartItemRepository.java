package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItems, UUID> {

    Optional<CartItems> findByCartIdAndProductId(UUID userId, UUID productId);

    List<CartItems> findByCartId(UUID cartId);

    @Query("""
            SELECT ci
            FROM CartItems ci
            WHERE ci.cartId = (
                SELECT c.cartId
                FROM Cart c
                WHERE c.userId = (
                    SELECT u.userId
                    FROM Users u
                    WHERE u.userEmail = :email
                )
            )
            """)
    List<CartItems> findByUserEmail(@Param("email") String userEmail);
}