package com.example.chekitoki.domain.token.model

import com.example.chekitoki.domain.user.model.User
import jakarta.persistence.*

@Entity
class RefreshToken(
    user: User,
    token: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User = user

    var token: String = token
}