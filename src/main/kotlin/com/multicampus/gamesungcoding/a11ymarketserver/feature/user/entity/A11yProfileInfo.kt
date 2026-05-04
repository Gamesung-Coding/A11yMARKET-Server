package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class A11yProfileInfo(
    profileName: String,
    description: String,
    contrastLevel: Int,
    textSizeLevel: Int,
    textSpacingLevel: Int,
    lineHeightLevel: Int,
    textAlign: String,
    screenReader: Boolean,
    smartContrast: Boolean,
    highlightLinks: Boolean,
    cursorHighlight: Boolean,
) {
    @Column(nullable = false, length = 50)
    var profileName: String = profileName
        private set

    @Column(length = 200)
    var description: String = description
        private set

    @Column(nullable = false)
    var contrastLevel: Int = contrastLevel
        private set

    @Column(nullable = false)
    var textSizeLevel: Int = textSizeLevel
        private set

    @Column(nullable = false)
    var textSpacingLevel: Int = textSpacingLevel
        private set

    @Column(nullable = false)
    var lineHeightLevel: Int = lineHeightLevel
        private set

    @Column(length = 10, nullable = false)
    var textAlign: String = textAlign
        private set

    @Column(nullable = false)
    var screenReader: Boolean = screenReader
        private set

    @Column(nullable = false)
    var smartContrast: Boolean = smartContrast
        private set

    @Column(nullable = false)
    var highlightLinks: Boolean = highlightLinks
        private set

    @Column(nullable = false)
    var cursorHighlight: Boolean = cursorHighlight
        private set
}
