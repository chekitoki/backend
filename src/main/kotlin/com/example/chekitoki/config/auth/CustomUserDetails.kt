package com.example.chekitoki.config.auth

import com.example.chekitoki.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails (
    private val user: User
) : UserDetails {
    override fun getAuthorities() = listOf(GrantedAuthority { user.role.value })
    override fun getPassword(): String  = user.password
    override fun getUsername(): String = user.userId

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}