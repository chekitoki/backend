package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.domain.goal.dto.GoalInfo
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.service.GoalRecordStore
import com.example.chekitoki.domain.user.service.UserStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val goalStore: GoalStore,
    private val goalRecordStore: GoalRecordStore,
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

        val savedGoal = goalStore.save(goal)
        goalRecordStore.findOrCreate(goal, null)

        return GoalInfo.Response(savedGoal)
    }

    fun getGoals(userId: String, info: GoalInfo.Read): List<GoalInfo.ResponseWithRecord> {
        val goals = goalStore.getByUserAndPeriod(userId, info.period)

        return goals.mapNotNull { goal ->
            val records = if (info.date == null) {
                goalRecordStore.findAllByGoal(goal)
            } else { goalRecordStore.findByGoalAndDate(goal, info.date)?.let { listOf(it) } }

            records?.takeIf { it.isNotEmpty() }?.let { GoalInfo.ResponseWithRecord(goal, it)}
        }
    }

    @Transactional
    fun updateGoal(userId: String, info: GoalInfo.Update): GoalInfo.Response {
        val goal = goalStore.getById(info.id)

        goalStore.checkGoalOwnership(goal, userId)

        goal.updateGoal(info.title, info.description, info.target, info.unit, info.period)
        return GoalInfo.Response(goalStore.save(goal))
    }

    @Transactional
    fun deleteGoal(userId: String, goalId: Long) {
        val goal = goalStore.getById(goalId)

        goalStore.checkGoalOwnership(goal, userId)

        goalStore.delete(goal)
    }
}