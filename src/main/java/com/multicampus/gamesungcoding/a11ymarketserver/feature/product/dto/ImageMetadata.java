package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ImageMetadata(
        @NotBlank String originalFileName,
        String altText,
        @NotNull @Min(0) Integer sequence,
        UUID imageId,
        Boolean isNew) {
}
