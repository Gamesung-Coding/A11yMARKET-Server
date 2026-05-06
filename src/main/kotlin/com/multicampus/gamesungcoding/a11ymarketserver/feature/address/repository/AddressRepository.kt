package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AddressRepository : JpaRepository<Addresses, UUID> {
    fun findByUserUserIdOrderByCreatedAtDesc(userId: UUID): List<Addresses>
    fun findByUserUserIdAndIsDefaultTrue(userId: UUID): Addresses?
    fun findByAddressIdAndUserUserId(addressId: UUID, userId: UUID): Addresses?
    fun findAllByUserUserId(userId: UUID): List<Addresses>
}