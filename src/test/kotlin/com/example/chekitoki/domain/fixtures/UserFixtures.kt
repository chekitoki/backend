package com.example.chekitoki.domain.fixtures

import com.example.chekitoki.domain.user.dto.UserInfo
import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.model.User

class UserFixtures {
    companion object {
        const val userId = "testuser"
        const val userEmail = "test@test.com"
        const val userName = "테스트"
        private const val userPassword = "asdfqwer1234"
        const val id = 1L

        const val modifiedName = "수정된테스트"
        private const val modifiedPassword = "qwer1234asdf"

        const val anotherUserId = "rnignon"

        val testUser = User(userId, userEmail, userName, userPassword)
        val modifiedProfileUser = User(userId, userEmail, modifiedName, userPassword)
        val modifiedPasswordUser = User(userId, userEmail, userName, modifiedPassword)

        /* request dto */
        val loginRequest = UserRequestDto.Login(userId, userPassword)
        val wrongLoginRequest = UserRequestDto.Login(userId, "wrong-password")

        val createRequest = UserRequestDto.Create(userId, userEmail, userName, userPassword)
        val wrongCreateRequest = UserRequestDto.Create("wrong!id?", "notemail", "123456789012345678901", "wrongpassword")

        val updateProfileRequest = UserRequestDto.UpdateProfile(modifiedName)

        val updatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, modifiedPassword)
        val wrongUpdatePasswordRequest = UserRequestDto.UpdatePassword(userPassword + "wrong", "")
        val duplicatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, userPassword)

        /* info */
        val loginInfo = loginRequest.toInfo()
        val wrongLoginInfo = wrongLoginRequest.toInfo()

        val createInfo = createRequest.toInfo()

        val updateProfileInfo = updateProfileRequest.toInfo()

        val updatePasswordInfo = updatePasswordRequest.toInfo()
        val wrongUpdatePasswordInfo = wrongUpdatePasswordRequest.toInfo()
        val duplicatePasswordInfo = duplicatePasswordRequest.toInfo()

        /* response */
        val UserResponseInfo = UserInfo.Response(id, userId, userEmail, userName)
        val UserUpdatedResponseInfo = UserInfo.Response(id, userId, userEmail, modifiedName)
    }
}