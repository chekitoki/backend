package com.example.chekitoki.domain.user.service

import com.example.chekitoki.config.PasswordEncoderWrapper
import com.example.chekitoki.domain.user.dto.UserInfo
import com.example.chekitoki.domain.user.exception.DuplicateUserException
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
        validateUserId(info.userId)
        val user = userStore.save(User(info.userId, info.email, info.name, encoder.encode(info.password)))
        return UserInfo.Response(user)
    }

    fun getUser(id: Long): UserInfo.Response {
        val user = userStore.getById(id)
        return UserInfo.Response(user)
    }

    @Transactional
    fun updateProfile(userId: String, info: UserInfo.UpdateProfile): UserInfo.Response {
        val user = userStore.getByUserId(userId)

        user.updateProfile(info.name)

        return UserInfo.Response(userStore.save(user))
    }

    @Transactional
    fun updatePassword(userId: String, info: UserInfo.UpdatePassword) {
        val user = userStore.getByUserId(userId)

        validatePasswordMatch(info.oldPassword, user.password)

        user.updatePassword(encoder.encode(info.newPassword))
        userStore.save(user)
    }

    @Transactional
    fun deleteUser(userId: String) {
        userStore.deleteByUserId(userId)
    }

    private fun validateUserId(userId: String) {
        if (userStore.existsByUserId(userId)) {
            throw DuplicateUserException("이미 존재하는 아이디입니다.")
        }
    }

    private fun validatePasswordMatch(oldPassword: String, newPassword: String) {
        if (!encoder.matches(oldPassword, newPassword)) {
            throw InvalidPasswordException("비밀번호가 일치하지 않습니다.")
        }
    }
}