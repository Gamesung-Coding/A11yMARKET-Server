package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductInquireResponse(UUID productId,
                                     String productName,
                                     Integer productPrice,
                                     Integer productStock,
                                     ProductStatus productStatus,
                                     String categoryName,
                                     LocalDateTime approvedAt,
                                     LocalDateTime updatedAt) {

    public static ProductInquireResponse fromEntity(Product product) {
        return new ProductInquireResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductStock(),
                product.getProductStatus(),
                product.getCategory().getCategoryName(),
                product.getApprovedDate(),
                product.getUpdatedAt()
        );
    }
}
