package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "USER_A11Y_PROFILES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserA11yProfile {

    @Id
    @UuidV7
    @Column(name = "PROFILE_ID", nullable = false, updatable = false, length = 16)
    private UUID profileId;

    @Column(name = "USER_ID", nullable = false, length = 16)
    private UUID userId;

    @Column(name = "PROFILE_NAME", nullable = false, length = 50)
    private String profileName;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Column(name = "CONTRAST_LEVEL", nullable = false)
    private Integer contrastLevel;

    @Column(name = "TEXT_SIZE_LEVEL", nullable = false)
    private Integer textSizeLevel;

    @Column(name = "TEXT_SPACING_LEVEL", nullable = false)
    private Integer textSpacingLevel;

    @Column(name = "LINE_HEIGHT_LEVEL", nullable = false)
    private Integer lineHeightLevel;

    @Column(name = "TEXT_ALIGN", nullable = false, length = 10)
    private String textAlign;

    @Column(name = "SCREEN_READER", nullable = false)
    private Integer screenReader;

    @Column(name = "SMART_CONTRAST", nullable = false)
    private Integer smartContrast;

    @Column(name = "HIGHLIGHT_LINKS", nullable = false)
    private Integer highlightLinks;

    @Column(name = "CURSOR_HIGHLIGHT", nullable = false)
    private Integer cursorHighlight;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private UserA11yProfile(
            UUID userId,
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
            Integer cursorHighlight
    ) {
        this.userId = userId;
        this.profileName = profileName;
        this.description = description;
        this.contrastLevel = contrastLevel;
        this.textSizeLevel = textSizeLevel;
        this.textSpacingLevel = textSpacingLevel;
        this.lineHeightLevel = lineHeightLevel;
        this.textAlign = textAlign;
        this.screenReader = screenReader;
        this.smartContrast = smartContrast;
        this.highlightLinks = highlightLinks;
        this.cursorHighlight = cursorHighlight;
    }

    public void update(
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
            Integer cursorHighlight
    ) {
        this.profileName = profileName;
        this.description = description;
        this.contrastLevel = contrastLevel;
        this.textSizeLevel = textSizeLevel;
        this.textSpacingLevel = textSpacingLevel;
        this.lineHeightLevel = lineHeightLevel;
        this.textAlign = textAlign;
        this.screenReader = screenReader;
        this.smartContrast = smartContrast;
        this.highlightLinks = highlightLinks;
        this.cursorHighlight = cursorHighlight;
    }

}
