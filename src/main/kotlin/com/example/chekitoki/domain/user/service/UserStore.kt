package com.example.chekitoki.domain.user.service

import com.example.chekitoki.domain.user.model.User

interface UserStore {
    fun findByEmail(email: String): User?
    fun getById(id: Long): User
    fun save(user: User): User
    fun deleteById(id: Long)
}