package com.example.chekitoki.domain.user.model

import com.example.chekitoki.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.Where

@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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

    var deleted: Boolean = false

    var refreshToken: String? = null

    var role: Role = Role.USER

    fun updateProfile(name: String) {
        this.name = name
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun updateRole(role: Role) {
        this.role = role
    }
}