package com.example.chekitoki.domain.token.repository

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode
import com.example.chekitoki.domain.token.model.RefreshToken
import com.example.chekitoki.domain.user.model.User
import org.springframework.stereotype.Service

@Service
class RefreshTokenStore(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun save(token: RefreshToken) {
        refreshTokenRepository.save(token)
    }

    fun getByToken(token: String): RefreshToken {
        return refreshTokenRepository.findByToken(token)
            ?: throw BusinessException(ResponseCode.NOT_FOUND, "RefreshToken not found.")
    }

    fun deleteByToken(token: String) {
        refreshTokenRepository.deleteByToken(token)
    }
}