package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerApplyResponse(
        UUID sellerId,
        String sellerName,
        String businessNumber,
        String sellerGrade,
        String sellerIntro,
        Boolean a11yGuarantee,
        String sellerSubmitStatus,
        LocalDateTime submitDate,
        LocalDateTime approvedDate) {
}
