package com.example.chekitoki.config.auth.jwt

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenProvider(
    @Value("\${spring.security.auth.secret}")
    private val secretKey: String,
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    companion object {
        const val ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 // 1H
        const val REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7 // 7D
        const val ACCESS_TOKEN = "access-token"
        const val REFRESH_TOKEN = "refresh-token"
    }

    class InvalidTokenException : BusinessException(ResponseCode.UNAUTHORIZED, "Token is not valid")

    fun generateToken(authentication: Authentication, isAccess: Boolean): String {
        val now = Date()
        val expirationDate = Date(now.time + if (isAccess) ACCESS_TOKEN_EXPIRE_LENGTH else REFRESH_TOKEN_EXPIRE_LENGTH)

        val username = authentication.name
        val role = authentication.authorities

        return Jwts.builder()
            .setSubject(username)
            .claim("role", role.map { it.authority })
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateTokenOrThrow(token: String) {
        if (!validateToken(token)) throw InvalidTokenException()
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
            ?: throw RuntimeException("Token is not valid")
    }
}