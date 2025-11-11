package com.multicampus.gamesungcoding.a11ymarketserver.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRespDTO {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userNickname;
    private String userRole;
}
