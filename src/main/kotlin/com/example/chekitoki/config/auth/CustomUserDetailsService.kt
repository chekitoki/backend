package com.example.chekitoki.config.auth

import com.example.chekitoki.domain.user.service.UserStore
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userStore: UserStore,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userStore.getByUserId(username)
        return CustomUserDetails(user)
    }
}