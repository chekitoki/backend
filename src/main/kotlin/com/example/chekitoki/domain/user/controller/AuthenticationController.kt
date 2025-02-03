package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.domain.user.dto.UserRequestDto
import com.example.chekitoki.domain.user.dto.UserResponseDto
import com.example.chekitoki.domain.user.service.AuthenticationService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController (
    private val authenticationService: AuthenticationService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: UserRequestDto.Login,
    ) : UserResponseDto {
        return authenticationService.login(request.toInfo()).toResponseDetail()
    }

    @DeleteMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userDetails: UserDetails,
    ) {
        // TODO: 로그아웃한 유저의 access token 블랙리스트 추가
        authenticationService.logout(userDetails)
    }

    @PatchMapping("/reissue")
    fun reissue() {
        authenticationService.reissue()
    }
}