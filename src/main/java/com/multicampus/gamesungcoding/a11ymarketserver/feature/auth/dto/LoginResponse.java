package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;

public record LoginResponse(String UserEmail,
                            String UserNickname,
                            String UserRole,
                            String accessToken,
                            String refreshToken) {
    public static LoginResponse fromEntityAndTokens(Users user,
                                                    String accessToken,
                                                    String refreshToken) {

        return new LoginResponse(
                user.getUserEmail(),
                user.getUserNickname(),
                user.getUserRole(),
                accessToken,
                refreshToken
        );
    }
}
