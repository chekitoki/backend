package com.example.chekitoki.domain.fixtures

import com.example.chekitoki.domain.user.dto.UserInfo
import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.model.User

class UserFixtures {
    companion object {
        const val userId = "testuser"
        const val userEmail = "test@test.com"
        const val userName = "테스트"
        const val userPassword = "asdfqwer1234"
        const val id = 1L

        const val modifiedName = "수정된테스트"
        private const val modifiedPassword = "qwer1234asdf"

        private const val anotherUserId = "rnignon"
        private const val anotherUserEmail = "rnignon@naver.com"
        private const val anotherUserName = "rnignon"
        private const val anotherUserPassword = "fdsarewq4321"
        private const val anotherId = 2L

        val testUser = User(userId, userEmail, userName, userPassword)
        val modifiedProfileUser = User(userId, userEmail, modifiedName, userPassword)
        val modifiedPasswordUser = User(userId, userEmail, userName, modifiedPassword)
        val anotherUser = User(anotherUserId, anotherUserEmail, anotherUserName, anotherUserPassword)

        /* request dto */
        val LoginRequest = UserRequestDto.Login(userId, userPassword)
        val WrongLoginRequest = UserRequestDto.Login(userId, "wrong-password")

        val CreateRequest = UserRequestDto.Create(userId, userEmail, userName, userPassword)
        val WrongCreateRequest = UserRequestDto.Create("wrong!id?", "notemail", "123456789012345678901", "wrongpassword")

        val UpdateProfileRequest = UserRequestDto.UpdateProfile(modifiedName)

        val UpdatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, modifiedPassword)
        val WrongUpdatePasswordRequest = UserRequestDto.UpdatePassword(userPassword + "wrong", "")
        val DuplicatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, userPassword)

        /* info */
        val LoginInfo = LoginRequest.toInfo()
        val WrongLoginInfo = WrongLoginRequest.toInfo()

        val CreateInfo = CreateRequest.toInfo()

        val UpdateProfileInfo = UpdateProfileRequest.toInfo()

        val UpdatePasswordInfo = UpdatePasswordRequest.toInfo()
        val WrongUpdatePasswordInfo = WrongUpdatePasswordRequest.toInfo()
        val DuplicatePasswordInfo = DuplicatePasswordRequest.toInfo()

        /* response */
        val UserResponseInfo = UserInfo.Response(id, userId, userEmail, userName)
        val UserUpdatedResponseInfo = UserInfo.Response(id, userId, userEmail, modifiedName)
        val DetailUserResponse = UserResponseInfo.toResponseDetail()
        val SummaryUserResponse = UserResponseInfo.toResponseSummary()
    }
}