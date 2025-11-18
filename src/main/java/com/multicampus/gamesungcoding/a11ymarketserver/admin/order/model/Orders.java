package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @UuidV7  // 커스텀 UUID v7 생성
    @Column(length = 16, updatable = false, nullable = false)
    private UUID orderId;

    @Column(length = 30)
    private String userName;

    @Column(length = 254)
    private String userEmail;

    @Column(length = 15)
    private String userPhone;

    @Column(length = 30)
    private String receiverName;

    @Column(length = 15)
    private String receiverPhone;

    @Column(columnDefinition = "CHAR(5)")
    private String receiverZipcode;

    @Column(length = 100)
    private String receiverAddr1;

    @Column(length = 200)
    private String receiverAddr2;

    @Column(length = 30)
    private String orderStatus;

    @Column
    private Integer totalPrice;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
