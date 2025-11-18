package com.multicampus.gamesungcoding.a11ymarketserver.order.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @UuidV7
    @Column(name = "ORDER_ID", length = 16, nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "USER_NAME", length = 30, nullable = false)
    private String userName;

    @Column(name = "USER_EMAIL", length = 150, nullable = false)
    private String userEmail;

    @Column(name = "USER_PHONE", length = 15, nullable = false)
    private String userPhone;

    @Column(name = "RECEIVER_NAME", length = 30, nullable = false)
    private String receiverName;

    @Column(name = "RECEIVER_PHONE", length = 15, nullable = false)
    private String receiverPhone;

    @Column(name = "RECEIVER_ZIPCODE", length = 5, nullable = false)
    private String receiverZipcode;

    @Column(name = "RECEIVER_ADDR1", length = 100, nullable = false)
    private String receiverAddr1;

    @Column(name = "RECEIVER_ADDR2", length = 200)
    private String receiverAddr2;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private Long totalPrice;

    @Column(name = "ORDER_STATUS", length = 30, nullable = false)
    private String orderStatus;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

}
