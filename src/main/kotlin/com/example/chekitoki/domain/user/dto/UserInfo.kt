package com.example.chekitoki.domain.user.dto

import com.example.chekitoki.domain.user.model.User

class UserInfo {
    data class Login(
        val email: String,
        val password: String,
    )

    data class Create(
        val email: String,
        val name: String,
        val password: String,
    )

    data class UpdateProfile(
        val id: Long,
        val name: String,
    )

    data class UpdatePassword(
        val id: Long,
        val oldPassword: String,
        val newPassword: String,
    )

    data class Response(
        val id: Long,
        val email: String,
        val name: String,
    ) {
        constructor(user: User): this(
            id = user.id,
            email = user.email,
            name = user.name,
        )

        fun toResponseDetail() = UserResponseDto.Detail(
            id = id,
            email = email,
            name = name,
        )

        fun toResponseSummary() = UserResponseDto.Summary(
            id = id,
            email = email,
            name = name,
        )
    }
}