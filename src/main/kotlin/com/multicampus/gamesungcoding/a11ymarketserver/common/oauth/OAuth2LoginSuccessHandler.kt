package com.multicampus.gamesungcoding.a11ymarketserver.common.oauth

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.OAuth2Properties
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserOauthLinks
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserOauthLinksRepository
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.nio.charset.StandardCharsets

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userOauthLinksRepository: UserOauthLinksRepository,
    private val oAuth2Properties: OAuth2Properties
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val kakaoId = (oAuth2User.attributes["id"] as? Long)?.toString()
            ?: throw IllegalArgumentException("카카오 ID를 찾을 수 없습니다.")

        val oauthLink = userOauthLinksRepository.findByOauthProviderId(kakaoId)
            ?: userOauthLinksRepository.save(
                UserOauthLinks(
                    oauthProvider = "KAKAO",
                    oauthProviderId = kakaoId,
                )
            )


        val user = oauthLink.user

        val targetUrl = if (user == null) {
            val tempJwt = jwtTokenProvider.createTemporaryAccessToken(oauthLink.userOauthLinkId!!)
            UriComponentsBuilder.fromUriString(oAuth2Properties.signupUri)
                .queryParam("temp_token", tempJwt)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString()
        } else {
            val jwt = jwtTokenProvider.createAccessToken(user.userId!!, user.userRole)
            UriComponentsBuilder.fromUriString(oAuth2Properties.redirectUri)
                .queryParam("token", jwt)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString()
        }
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
