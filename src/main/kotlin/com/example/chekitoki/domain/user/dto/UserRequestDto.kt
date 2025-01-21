package com.example.chekitoki.domain.user.dto

class UserRequestDto {
    data class Login(
        val email: String,
        val password: String,
    ) {
        fun toInfo() = UserInfo.Login(
            email = email,
            password = password,
        )
    }

    data class Create(
        val email: String,
        val name: String,
        val password: String,
    ) {
        fun toInfo() = UserInfo.Create(
            email = email,
            name = name,
            password = password,
        )
    }

    data class UpdateProfile(
        val userId: Long,
        val name: String,
    ) {
        fun toInfo() = UserInfo.UpdateProfile(
            id = userId,
            name = name,
        )
    }

    data class UpdatePassword(
        val userId: Long,
        val oldPassword: String,
        val newPassword: String,
    ) {
        fun toInfo() = UserInfo.UpdatePassword(
            id = userId,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }
}