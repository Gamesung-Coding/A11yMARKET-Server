package com.multicampus.gamesungcoding.a11ymarketserver.common.config

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.filter.JwtAuthenticationFilter
import com.multicampus.gamesungcoding.a11ymarketserver.common.oauth.OAuth2LoginSuccessHandler
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.CorsProperties
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val corsProperties: CorsProperties,
    private val authenticationFilter: JwtAuthenticationFilter,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authCfg: AuthenticationConfiguration): AuthenticationManager {
        return authCfg.authenticationManager
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cfg = CorsConfiguration().apply {
            allowedOriginPatterns = corsProperties.allowedOriginPatterns
            addAllowedHeader("*")
            addAllowedMethod("*")
            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", cfg)
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { configurationSource = corsConfigurationSource() }
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests {
                authorize("/actuator/health", permitAll)
                authorize("/api/*/auth/login", permitAll)
                authorize("/api/*/auth/login-refresh", permitAll)
                authorize("/api/*/auth/join", permitAll)
                authorize("/api/*/auth/check/**", permitAll)
                authorize("/api/*/auth/refresh", permitAll)
                authorize("/api/*/auth/oauth2/code/kakao", permitAll)
                authorize("/api/*/categories/**", permitAll)
                authorize("/api/*/products/**", permitAll)
                authorize("/api/*/seller/info/**", permitAll)
                authorize("/api/*/main/**", permitAll)
                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/connection/test", permitAll)

                authorize("/api/*/seller/apply", hasRole("USER"))
                authorize("/api/*/seller/**", hasRole("SELLER"))
                authorize("/api/*/admin/**", hasRole("ADMIN"))

                authorize(anyRequest, authenticated)
            }
            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint { _, response, authException ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json;charset=UTF-8"
                    response.writer.write(
                        "{\"error\": \"Unauthorized\", \"message\": \"${authException.message}\"}"
                    )
                }
            }
            oauth2Login {
                authenticationSuccessHandler = oAuth2LoginSuccessHandler
            }
        }

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
