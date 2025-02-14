package com.example.chekitoki.domain.goal.repository

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<Goal,Long> {
    fun findByUserUserId(userId: String): List<Goal>
    fun findByUserUserIdAndPeriod(userId: String, period: GoalPeriod): List<Goal>
}