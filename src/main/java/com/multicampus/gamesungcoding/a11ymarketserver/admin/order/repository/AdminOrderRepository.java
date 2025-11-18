package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminOrderRepository extends JpaRepository<Orders, UUID> {
}
