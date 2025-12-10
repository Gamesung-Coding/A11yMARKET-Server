package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerProfileResponse(UUID sellerId,
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
                                    LocalDateTime lastUpdatedDate) {

    public static SellerProfileResponse fromEntity(Seller seller) {
        var user = seller.getUser();

        return new SellerProfileResponse(
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
                seller.getUpdatedAt()
        );
    }
}
