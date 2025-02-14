package com.example.chekitoki.domain.goal.repository

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.exception.NoSuchGoalException
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goal.service.GoalStore
import org.springframework.stereotype.Component

@Component
class GoalStoreImpl(
    private val goalRepository: GoalRepository,
) : GoalStore {
    override fun save(goal: Goal): Goal {
        return goalRepository.save(goal)
    }

    override fun getById(id: Long): Goal {
        return goalRepository.findById(id)
            .orElseThrow { throw NoSuchGoalException("해당 목표를 찾을 수 없습니다.") }
    }

    override fun getByUserAndPeriod(userId: String, period: GoalPeriod?): List<Goal> {
        return if (period == null) {
            goalRepository.findByUserUserId(userId)
        } else {
            goalRepository.findByUserUserIdAndPeriod(userId, period)
        }
    }

    override fun delete(goal: Goal) {
        goalRepository.delete(goal)
    }
}