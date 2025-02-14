package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal

interface GoalStore {
    fun save(goal: Goal): Goal
    fun getById(id: Long): Goal
    fun getByUserAndPeriod(userId: String, period: GoalPeriod): List<Goal>
    fun delete(goal: Goal)
    fun checkGoalOwnership(goal: Goal, userId: String)
}