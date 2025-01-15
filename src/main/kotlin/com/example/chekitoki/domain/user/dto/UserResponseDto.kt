package com.example.chekitoki.domain.user.dto

sealed class UserResponseDto {
    data class Summary(
        val id: Long,
        val email: String,
        val name: String,
    ) : UserResponseDto()

    data class Detail(
        val id: Long,
        val email: String,
        val name: String,
    ) : UserResponseDto()
}