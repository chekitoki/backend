package com.example.chekitoki.domain.user.dto

class UserRequestDto {
    data class Login(
        val userId: String,
        val password: String,
    ) {
        fun toInfo() = UserInfo.Login(
            userId = userId,
            password = password,
        )
    }

    data class Create(
        val userId: String,
        val email: String,
        val name: String,
        val password: String,
    ) {
        fun toInfo() = UserInfo.Create(
            userId = userId,
            email = email,
            name = name,
            password = password,
        )
    }

    data class UpdateProfile(
        val id: Long,
        val name: String,
    ) {
        fun toInfo() = UserInfo.UpdateProfile(
            id = id,
            name = name,
        )
    }

    data class UpdatePassword(
        val id: Long,
        val oldPassword: String,
        val newPassword: String,
    ) {
        fun toInfo() = UserInfo.UpdatePassword(
            id = id,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }
}