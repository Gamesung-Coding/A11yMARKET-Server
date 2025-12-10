package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderItemResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductInquireResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SellerDetailResponse(UUID sellerId,
                                   String sellerName,
                                   String businessNumber,
                                   SellerGrades sellerGrade,
                                   String contactEmail,
                                   String contactPhone,
                                   String storeIntro,
                                   Boolean isA11yGuarantee,
                                   SellerSubmitStatus profileStatus,
                                   LocalDateTime submitDate,
                                   LocalDateTime approvedDate,
                                   LocalDateTime lastUpdatedDate,
                                   List<OrderItemResponse> orders,
                                   List<ProductInquireResponse> products) {

    public static SellerDetailResponse fromEntity(Seller seller, List<OrderItems> ordersList) {
        var user = seller.getUser();

        return new SellerDetailResponse(
                seller.getSellerId(),
                seller.getSellerName(),
                seller.getBusinessNumber(),
                seller.getSellerGrade(),
                user.getUserEmail(),
                user.getUserPhone(),
                seller.getSellerIntro(),
                seller.getIsA11yGuarantee(),
                seller.getSellerSubmitStatus(),
                seller.getSubmitDate(),
                seller.getApprovedDate(),
                seller.getUpdatedAt(),
                ordersList.stream()
                        .map(OrderItemResponse::fromEntity)
                        .toList(),
                seller.getProducts().stream()
                        .map(ProductInquireResponse::fromEntity)
                        .toList()
        );
    }
}
