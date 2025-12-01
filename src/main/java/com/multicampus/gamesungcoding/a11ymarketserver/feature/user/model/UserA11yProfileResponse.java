package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserA11yProfileResponse(
        UUID profileId,
        String profileName,
        String description,
        Integer contrastLevel,
        Integer textSizeLevel,
        Integer textSpacingLevel,
        Integer lineHeightLevel,
        String textAlign,
        Integer screenReader,
        Integer smartContrast,
        Integer highlightLinks,
        Integer cursorHighlight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserA11yProfileResponse fromEntity(UserA11yProfile p) {
        return new UserA11yProfileResponse(
                p.getProfileId(),
                p.getProfileName(),
                p.getDescription(),
                p.getContrastLevel(),
                p.getTextSizeLevel(),
                p.getTextSpacingLevel(),
                p.getLineHeightLevel(),
                p.getTextAlign(),
                p.getScreenReader(),
                p.getSmartContrast(),
                p.getHighlightLinks(),
                p.getCursorHighlight(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
