package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.dto.UserResponseDto
import com.example.chekitoki.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController (
    private val userService: UserService
) {
    @PostMapping
    fun createUser(
        @Valid @RequestBody request: UserRequestDto.Create,
    ): UserResponseDto {
        val response = userService.createUser(request.toInfo())
        return response.toResponseDetail()
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long,
    ): UserResponseDto {
        val response = userService.getUser(userId)
        return response.toResponseDetail()
    }

    @PatchMapping("/update/profile")
    fun updateUserProfile(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: UserRequestDto.UpdateProfile,
    ): UserResponseDto {
        val response = userService.updateProfile(userDetails.user, request.toInfo())
        return response.toResponseDetail()
    }

    @PatchMapping("/update/password")
    fun updateUserPassword(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: UserRequestDto.UpdatePassword,
    ) {
        return userService.updatePassword(userDetails.user, request.toInfo())
    }

    @DeleteMapping
    fun deleteUser(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ) {
        userService.deleteUser(userDetails.user)
    }
}