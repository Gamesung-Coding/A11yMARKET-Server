package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    jwtProperties: JwtProperties,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val secretKey: SecretKey = jwtProperties.secretKey
    private val accessTokenValidityMs: Long = jwtProperties.accessTokenValidityMs

    fun createAccessToken(authentication: Authentication): String {
        // e.g., "ROLE_USER,ROLE_ADMIN"
        val authorities = authentication
            .authorities
            .joinToString(",") { it.authority ?: "" }

        return createToken(authentication.name, authorities)
    }

    fun createAccessToken(userId: UUID, role: UserRole): String {
        return createToken(userId.toString(), role.name)
    }

    fun createTemporaryAccessToken(uuid: UUID): String {
        val now = Date()
        val expiryDate = Date(now.time + 15 * 60 * 1000) // 15 minutes

        return Jwts.builder()
            .subject(uuid.toString())
            .claim("auth", UserRole.TEMP.name)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        val authClaim = claims["auth"]?.toString() ?: ""
        val authorities = authClaim.split(",")
            .filter { it.isNotBlank() }
            .map { SimpleGrantedAuthority(it) }

        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, principal.authorities)
    }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            log.debug("JwtTokenProvider - validateToken")
            true
        } catch (e: MalformedJwtException) {
            log.debug("JwtTokenProvider.getAuthentication - Invalid JWT token format: ${e.message}")
            false
        } catch (e: ExpiredJwtException) {
            log.debug("JwtTokenProvider.getAuthentication - Expired JWT token: ${e.message}")
            false
        } catch (e: SecurityException) {
            log.debug("JwtTokenProvider.getAuthentication - Invalid JWT signature: ${e.message}")
            false
        } catch (e: Exception) {
            log.debug("JwtTokenProvider.getAuthentication - JWT token validation error: ${e.message}")
            false
        }
    }

    private fun createToken(subject: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenValidityMs)

        return Jwts.builder()
            .subject(subject)
            .claim("auth", role)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }
}
