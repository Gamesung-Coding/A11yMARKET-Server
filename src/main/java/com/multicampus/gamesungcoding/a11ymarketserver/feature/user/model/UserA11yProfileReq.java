package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import jakarta.validation.constraints.*;

public record UserA11yProfileReq(
        @NotBlank(message = "프로필 이름은 필수 입력 값입니다.")
        @Size(max = 50, message = "프로필 이름은 최대 50자까지 가능합니다.")
        String profileName,

        @Size(max = 200, message = "설명은 최대 200자까지 가능합니다.")
        String description,

        @NotNull(message = "대비 설정 값은 필수입니다.") @Min(0) @Max(3)
        Integer contrastLevel,

        @NotNull(message = "글자 크기 설정 값은 필수입니다.") @Min(0) @Max(2)
        Integer textSizeLevel,

        @NotNull(message = "글자 간격 설정 값은 필수입니다.") @Min(0) @Max(2)
        Integer textSpacingLevel,

        @NotNull(message = "줄 간격 설정 값은 필수입니다.") @Min(0) @Max(2)
        Integer lineHeightLevel,

        @NotBlank(message = "텍스트 정렬은 필수입니다.")
        @Pattern(regexp = "^(left|center|right)$", message = "텍스트 정렬은 left, center, right 중 하나여야 합니다.")
        String textAlign,

        @NotNull(message = "스크린리더 설정 값은 필수입니다.")
        Boolean screenReader,

        @NotNull(message = "스마트 대비 설정 값은 필수입니다.")
        Boolean smartContrast,

        @NotNull(message = "링크 강조 설정 값은 필수입니다.")
        Boolean highlightLinks,

        @NotNull(message = "커서 강조 설정 값은 필수입니다.")
        Boolean cursorHighlight) {


}
