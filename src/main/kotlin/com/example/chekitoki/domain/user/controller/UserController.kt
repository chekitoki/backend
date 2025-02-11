package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.dto.UserResponseDto
import com.example.chekitoki.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: UserRequestDto.UpdateProfile,
    ): UserResponseDto {
        val response = userService.updateProfile(userDetails.username, request.toInfo())
        return response.toResponseDetail()
    }

    @PatchMapping("/update/password")
    fun updateUserPassword(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: UserRequestDto.UpdatePassword,
    ) {
        return userService.updatePassword(userDetails.username, request.toInfo())
    }

    @DeleteMapping
    fun deleteUser(
        @AuthenticationPrincipal userDetails: UserDetails,
    ) {
        userService.deleteUser(userDetails.username)
    }
}