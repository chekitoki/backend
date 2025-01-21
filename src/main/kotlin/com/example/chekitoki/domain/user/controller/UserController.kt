package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.dto.UserResponseDto
import com.example.chekitoki.domain.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController (
    private val userService: UserService
) {
    @PostMapping
    fun createUser(
        @RequestBody request: UserRequestDto.Create,
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
        @RequestBody request: UserRequestDto.UpdateProfile,
    ): UserResponseDto {
        val response = userService.updateProfile(request.toInfo())
        return response.toResponseDetail()
    }

    @PatchMapping("/update/password")
    fun updateUserPassword(
        @RequestBody request: UserRequestDto.UpdatePassword,
    ) {
        return userService.updatePassword(request.toInfo())
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
    ) {
        userService.deleteUser(userId)
    }
}