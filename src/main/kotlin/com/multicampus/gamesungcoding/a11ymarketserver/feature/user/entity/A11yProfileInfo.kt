package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class A11yProfileInfo(
    @Column(nullable = false, length = 50)
    var profileName: String,

    @Column(length = 200)
    var description: String,

    @Column
    var isPreset: Boolean = false,

    @Column(nullable = false)
    var contrastLevel: Int,

    @Column(nullable = false)
    var textSizeLevel: Int,

    @Column(nullable = false)
    var textSpacingLevel: Int,

    @Column(nullable = false)
    var lineHeightLevel: Int,

    @Column(length = 10, nullable = false)
    var textAlign: String,

    @Column(nullable = false)
    var screenReader: Boolean,

    @Column(nullable = false)
    var smartContrast: Boolean,

    @Column(nullable = false)
    var highlightLinks: Boolean,

    @Column(nullable = false)
    var cursorHighlight: Boolean,
)
