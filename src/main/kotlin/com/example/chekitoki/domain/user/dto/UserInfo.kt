package com.example.chekitoki.domain.user.dto

import com.example.chekitoki.domain.user.model.User

class UserInfo {
    data class Login(
        val userId: String,
        val password: String,
    )

    data class Create(
        val userId: String,
        val email: String,
        val name: String,
        val password: String,
    )

    data class UpdateProfile(
        val name: String,
    )

    data class UpdatePassword(
        val oldPassword: String,
        val newPassword: String,
    )

    data class Response(
        val id: Long,
        val userId: String,
        val email: String,
        val name: String,
    ) {
        constructor(user: User): this(
            id = user.id,
            userId = user.userId,
            email = user.email,
            name = user.name,
        )

        fun toResponseDetail() = UserResponseDto.Detail(
            id = id,
            userId = userId,
            email = email,
            name = name,
        )

        fun toResponseSummary() = UserResponseDto.Summary(
            id = id,
            userId = userId,
            name = name,
        )
    }
}