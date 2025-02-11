package com.example.chekitoki.domain.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class UserRequestDto {
    // TODO: !!를 사용하지 않고, NotBlank를 처리할 수 있는 방안 모색
    companion object {
        const val NAME_REGEX = "^[a-zA-Z가-힣]{2,20}$"
        const val NAME_MESSAGE = "이름은 2~20자의 한글 또는 영문으로 이루어져야 합니다."

        const val PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$"
        const val PASSWORD_MESSAGE = "비밀번호는 8~20자의 영문 대소문자와 숫자로 이루어져야 합니다."
    }

    data class Login(
        @field:NotBlank(message = "아이디를 입력해주세요.")
        val userId: String?,
        @field:NotBlank(message = "비밀번호를 입력해주세요.")
        val password: String?,
    ) {
        fun toInfo() = UserInfo.Login(
            userId = userId!!,
            password = password!!,
        )
    }

    data class Create(
        @field:NotBlank(message = "아이디는 필수 입력값입니다.")
        @field:Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 영문 소문자와 숫자로 이루어져야 합니다.")
        val userId: String?,
        @field:NotBlank(message = "이메일은 필수 입력값입니다.")
        @field:Pattern(regexp = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        val email: String?,
        @field:NotBlank(message = "이름은 필수 입력값입니다.")
        @field:Pattern(regexp = NAME_REGEX, message = NAME_MESSAGE)
        val name: String?,
        @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @field:Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
        val password: String?,
    ) {
        fun toInfo() = UserInfo.Create(
            userId = userId!!,
            email = email!!,
            name = name!!,
            password = password!!,
        )
    }

    data class UpdateProfile(
        @field:NotBlank(message = "이름은 필수 입력값입니다.")
        @field:Pattern(regexp = NAME_REGEX, message = NAME_MESSAGE)
        val name: String?,
    ) {
        fun toInfo(): UserInfo.UpdateProfile {
            return UserInfo.UpdateProfile(
                name = name!!,
            )
        }
    }

    data class UpdatePassword(
        @field:NotBlank(message = "이전 비밀번호는 필수 입력값입니다.")
        val oldPassword: String?,
        @field:NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
        @field:Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
        val newPassword: String?,
    ) {
        fun toInfo(): UserInfo.UpdatePassword {
            return UserInfo.UpdatePassword(
                oldPassword = oldPassword!!,
                newPassword = newPassword!!,
            )
        }
    }
}