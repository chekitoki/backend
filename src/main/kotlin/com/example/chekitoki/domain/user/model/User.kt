package com.example.chekitoki.domain.user.model

import com.example.chekitoki.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User (
    email: String,
    name: String,
    password: String,
) : BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val email: String = email

    var name: String = name

    var password: String = password

    fun updateProfile(name: String) {
        this.name = name
    }

    fun updatePassword(password: String) {
        this.password = password
    }
}