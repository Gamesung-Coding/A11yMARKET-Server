package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

public enum SellerOrderClaimProcessStatus {
    APPROVED,
    REJECTED;

    public boolean isApproved() {
        return this == APPROVED;
    }
}
