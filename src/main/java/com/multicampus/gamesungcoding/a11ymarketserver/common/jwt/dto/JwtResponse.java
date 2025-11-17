package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtResponse {
    String accessToken;
    String refreshToken;
}
