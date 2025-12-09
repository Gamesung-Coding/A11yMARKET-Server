package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;

import java.util.UUID;

public record ProductImageResponse(UUID imageId, String imageUrl, String altText, Integer imageSequence) {
    public static ProductImageResponse fromEntity(ProductImages image) {
        return new ProductImageResponse(
                image.getImageId(),
                image.getImageUrl(),
                image.getAltText(),
                image.getImageSequence()
        );
    }
}
