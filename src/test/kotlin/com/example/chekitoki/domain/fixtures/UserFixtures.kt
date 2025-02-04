package com.example.chekitoki.domain.fixtures

import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.model.User

class UserFixtures {
    companion object {
        const val userId = "test"
        const val userEmail = "test@test.com"
        const val userName = "테스트"
        const val userPassword = "asdfqwer1234"
        const val id = 1L

        private const val modifiedName = "수정된 테스트"
        private const val modifiedPassword = "qwer1234asdf"

        private const val anotherUserId = "rnignon"
        private const val anotherUserEmail = "rnignon@naver.com"
        private const val anotherUserName = "rnignon"
        private const val anotherUserPassword = "fdsarewq4321"
        private const val anotherId = 2L

        val testUser = User(userId, userEmail, userName, userPassword)
        val modifiedUser = User(userId, userEmail, modifiedName, modifiedPassword)
        val anotherUser = User(anotherUserId, anotherUserEmail, anotherUserName, anotherUserPassword)

        /* request dto */
        val LoginRequest = UserRequestDto.Login(userId, userPassword)
        val WrongLoginRequest = UserRequestDto.Login(userId, "wrong-password")

        val CreateRequest = UserRequestDto.Create(userId, userEmail, userName, userPassword)

        val UpdateProfileRequest = UserRequestDto.UpdateProfile(modifiedName)

        val UpdatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, modifiedPassword)
        val WrongUpdatePasswordRequest = UserRequestDto.UpdatePassword(userPassword + "wrong", modifiedPassword)
        val DuplicatePasswordRequest = UserRequestDto.UpdatePassword(userPassword, userPassword)

        /* info */
        val LoginInfo = LoginRequest.toInfo()
        val WrongLoginInfo = WrongLoginRequest.toInfo()

        val CreateInfo = CreateRequest.toInfo()

        val UpdateProfileInfo = UpdateProfileRequest.toInfo()

        val UpdatePasswordInfo = UpdatePasswordRequest.toInfo()
        val WrongUpdatePasswordInfo = WrongUpdatePasswordRequest.toInfo()
        val DuplicatePasswordInfo = DuplicatePasswordRequest.toInfo()
    }
}