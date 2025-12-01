package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserDeleteRequest(
        @NotNull
        String userPassword) {
}
