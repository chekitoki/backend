package com.example.chekitoki.domain.goal.repository

import com.example.chekitoki.api.exception.ResourceAuthorizationException
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

    override fun findAllByPeriod(period: GoalPeriod): List<Goal> {
        return goalRepository.findAllByPeriod(period)
    }

    override fun getById(id: Long): Goal {
        return goalRepository.findById(id)
            .orElseThrow { throw NoSuchGoalException("해당 목표를 찾을 수 없습니다.") }
    }

    override fun getByUserAndPeriod(userId: String, period: GoalPeriod): List<Goal> {
        val periods = when (period) {
            GoalPeriod.DAILY -> listOf(GoalPeriod.DAILY, GoalPeriod.WEEKLY, GoalPeriod.MONTHLY)
            GoalPeriod.WEEKLY -> listOf(GoalPeriod.WEEKLY, GoalPeriod.MONTHLY)
            GoalPeriod.MONTHLY -> listOf(GoalPeriod.MONTHLY)
        }
        return goalRepository.findByUserUserIdAndPeriod(userId, periods)
    }

    override fun delete(goal: Goal) {
        goalRepository.delete(goal)
    }

    override fun checkGoalOwnership(goal: Goal, userId: String) {
        if (goal.user.userId != userId) {
            throw ResourceAuthorizationException("해당 목표에 대한 권한이 없습니다.")
        }
    }
}