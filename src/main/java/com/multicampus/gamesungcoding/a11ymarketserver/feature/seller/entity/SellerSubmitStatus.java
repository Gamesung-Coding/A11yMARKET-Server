package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity;

public enum SellerSubmitStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }
}
