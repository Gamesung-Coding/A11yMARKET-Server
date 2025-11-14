package com.multicampus.gamesungcoding.a11ymarketserver.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PwdResetConfirmDTO {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private UUID userId;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "형식에 맞는 이메일 주소여야 합니다.")
    private String email;

    @NotBlank(message = "재설정 토큰은 필수입니다.")
    private String resetToken;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
//    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
//    )
    private String newPassword;
}
