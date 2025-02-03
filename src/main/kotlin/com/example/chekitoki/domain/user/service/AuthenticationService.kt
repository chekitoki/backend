package com.example.chekitoki.domain.user.service

import com.example.chekitoki.config.auth.jwt.TokenProvider
import com.example.chekitoki.domain.token.model.RefreshToken
import com.example.chekitoki.domain.token.repository.RefreshTokenStore
import com.example.chekitoki.domain.user.dto.UserInfo
import com.example.chekitoki.utils.CookieUtils
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val tokenStore: RefreshTokenStore,
    private val tokenProvider: TokenProvider,
    private val userStore: UserStore,
) {
    @Transactional
    fun login(info: UserInfo.Login): UserInfo.Response {
        val authentication = authenticate(info.userId, info.password)

        val accessToken = tokenProvider.generateToken(authentication, true)
        val refreshToken = tokenProvider.generateToken(authentication, false)

        val user = userStore.getByUserId(info.userId)
        tokenStore.save(RefreshToken(user, refreshToken))

        addCookies(accessToken, refreshToken)

        return UserInfo.Response(user)
    }

    @Transactional
    fun logout(userDetails: UserDetails) {
        val accessToken = CookieUtils.getCookie(TokenProvider.ACCESS_TOKEN)
        val refreshToken = CookieUtils.getCookie(TokenProvider.REFRESH_TOKEN)

        tokenStore.deleteByToken(refreshToken.value)

        // TODO: 로그아웃한 유저의 access token 블랙리스트 추가
        CookieUtils.deleteCookie(accessToken)
        CookieUtils.deleteCookie(refreshToken)
    }

    @Transactional
    fun reissue() {
        val refreshToken = (CookieUtils.getCookie(TokenProvider.REFRESH_TOKEN) ?: throw RuntimeException("Refresh token is not found")).value

        if (!tokenProvider.validateToken(refreshToken)) {
            throw RuntimeException("Refresh token is not valid")
        }

        val claims = tokenProvider.getClaims(refreshToken)
        val user = userStore.getByUserId(claims.subject)
        val roles = (claims["role"] as List<String>).map { SimpleGrantedAuthority(it) }

        val authentication = UsernamePasswordAuthenticationToken(user.userId, null, roles)

        val newAccessToken = tokenProvider.generateToken(authentication, true)
        val newRefreshToken = tokenProvider.generateToken(authentication, false)

        tokenStore.getByToken(refreshToken).token = newRefreshToken

        addCookies(newAccessToken, newRefreshToken)
    }

    private fun authenticate(username: String, password: String): Authentication {
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(authenticationToken)
    }

    private fun addCookies(accessToken: String, refreshToken: String) {
        CookieUtils.addCookie(TokenProvider.ACCESS_TOKEN, accessToken, TokenProvider.ACCESS_TOKEN_EXPIRE_LENGTH.toInt())
        CookieUtils.addCookie(TokenProvider.REFRESH_TOKEN, refreshToken, TokenProvider.REFRESH_TOKEN_EXPIRE_LENGTH.toInt())
    }
}