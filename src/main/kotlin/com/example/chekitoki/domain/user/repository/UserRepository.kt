package com.example.chekitoki.domain.user.repository

import com.example.chekitoki.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUserId(userId: String): User?
}