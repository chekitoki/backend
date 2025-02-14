package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.api.exception.ResourceAuthorizationException
import com.example.chekitoki.domain.goal.dto.GoalInfo
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.user.service.UserStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val goalStore: GoalStore,
//    private val goalRecordStore: GoalRecordStore,
    private val userStore: UserStore,
) {
    @Transactional
    fun createGoal(userId: String, info: GoalInfo.Create): GoalInfo.Response {
        val user = userStore.getByUserId(userId)
        val goal = Goal(
            user = user,
            title = info.title,
            description = info.description,
            target = info.target,
            unit = info.unit,
            period = info.period,
        )

        return GoalInfo.Response(goalStore.save(goal))
    }

    fun getGoals(userId: String, info: GoalInfo.Read): List<GoalInfo.Response> {
        val goals = goalStore.getByUserAndPeriod(userId, info.period)
        // date가 있을 시, 해당 date에 대한 record를 가져와서 반환

        return goals.map { GoalInfo.Response(it) }
    }

    fun getGoal(userId: String, goalId: Long): GoalInfo.Response {
        val goal = goalStore.getById(goalId)
        checkGoalOwnership(userId, goal)
        // TODO: record를 포함하여 반환

        return GoalInfo.Response(goal)
    }

    @Transactional
    fun updateGoal(userId: String, info: GoalInfo.Update): GoalInfo.Response {
        val goal = goalStore.getById(info.id)
        checkGoalOwnership(userId, goal)

        goal.updateGoal(info.title, info.description, info.target, info.unit, info.period)

        return GoalInfo.Response(goalStore.save(goal))
    }

    @Transactional
    fun deleteGoal(userId: String, goalId: Long) {
        val goal = goalStore.getById(goalId)
        checkGoalOwnership(userId, goal)

        goalStore.delete(goal)
    }

    private fun checkGoalOwnership(userId: String, goal: Goal) {
        if (userId != goal.user.userId) {
            throw ResourceAuthorizationException("해당 목표에 대한 권한이 없습니다.")
        }
    }
}