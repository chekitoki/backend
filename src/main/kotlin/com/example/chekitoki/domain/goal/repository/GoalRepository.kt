package com.example.chekitoki.domain.goal.repository

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<Goal,Long> {
    fun findAllByPeriod(period: GoalPeriod): List<Goal>
    @Query("SELECT g FROM Goal g WHERE g.user.userId = :userId AND g.period IN :periods")
    fun findByUserUserIdAndPeriod(userId: String, periods: List<GoalPeriod>): List<Goal>
}