package com.example.chekitoki.domain.user.repository

import com.example.chekitoki.domain.user.exception.NoSuchUserException
import com.example.chekitoki.domain.user.model.User
import com.example.chekitoki.domain.user.service.UserStore
import org.springframework.stereotype.Component

@Component
class UserStoreImpl(
    private val userRepository: UserRepository,
) : UserStore {
    override fun existsByUserId(userId: String): Boolean {
        return userRepository.existsByUserId(userId)
    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun getById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { NoSuchUserException("$id 에 대한 회원 정보가 존재하지 않습니다.") }
    }

    override fun getByUserId(userId: String): User {
        return userRepository.findByUserId(userId)
            ?: throw NoSuchUserException("$userId 에 대한 회원 정보가 존재하지 않습니다.")
    }

    override fun getByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw NoSuchUserException("$email 에 대한 회원 정보가 존재하지 않습니다.")
    }

    override fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun deleteByUserId(userId: String) {
        userRepository.deleteByUserId(userId)
    }
}