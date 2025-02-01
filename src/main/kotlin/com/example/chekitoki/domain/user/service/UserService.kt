package com.example.chekitoki.domain.user.service

import com.example.chekitoki.config.PasswordEncoderWrapper
import com.example.chekitoki.domain.user.dto.UserInfo
import com.example.chekitoki.domain.user.dto.UserResponseDto
import com.example.chekitoki.domain.user.exception.InvalidPasswordException
import com.example.chekitoki.domain.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userStore: UserStore,
    private val encoder: PasswordEncoderWrapper
) {
    @Transactional
    fun createUser(info: UserInfo.Create): UserInfo.Response {
        val user = userStore.save(User(info.userId, info.email, info.name, encoder.encode(info.password)))
        return UserInfo.Response(user)
    }

    fun getUser(id: Long): UserInfo.Response {
        val user = userStore.getById(id)
        return UserInfo.Response(user)
    }

    @Transactional
    fun updateProfile(info: UserInfo.UpdateProfile): UserInfo.Response {
        val user = userStore.getById(info.id)

        user.updateProfile(info.name)

        return UserInfo.Response(userStore.save(user))
    }

    @Transactional
    fun updatePassword(info: UserInfo.UpdatePassword) {
        val user = userStore.getById(info.id)

        confirmPassword(info.oldPassword, user.password)

        user.updatePassword(encoder.encode(info.newPassword))
        userStore.save(user)
    }

    @Transactional
    fun deleteUser(id: Long) {
        userStore.deleteById(id)
    }

    private fun confirmPassword(oldPassword: String, newPassword: String) {
        if (!encoder.matches(oldPassword, newPassword)) {
            throw InvalidPasswordException("비밀번호가 일치하지 않습니다.")
        }
    }
}