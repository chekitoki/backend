package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.domain.goal.dto.GoalInfo
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.service.GoalRecordStore
import com.example.chekitoki.domain.user.model.User
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
    fun createGoal(user: User, info: GoalInfo.Create): GoalInfo.Response {
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

    fun getGoals(user: User, info: GoalInfo.Read): List<GoalInfo.ResponseWithRecord> {
        val goals = goalStore.getByUserAndPeriod(user.id, info.period)

        return goals.mapNotNull { goal ->
            val records = if (info.date == null) {
                goalRecordStore.findAllByGoal(goal)
            } else { goalRecordStore.findByGoalAndDate(goal, info.date)?.let { listOf(it) } }

            records?.takeIf { it.isNotEmpty() }?.let { GoalInfo.ResponseWithRecord(goal, it)}
        }
    }

    @Transactional
    fun updateGoal(user: User, info: GoalInfo.Update): GoalInfo.Response {
        val goal = goalStore.getById(info.id)

        goalStore.checkGoalOwnership(goal, user.id)

        goal.updateGoal(info.title, info.description, info.target, info.unit)
        return GoalInfo.Response(goalStore.save(goal))
    }

    @Transactional
    fun deleteGoal(user: User, goalId: Long) {
        val goal = goalStore.getById(goalId)

        goalStore.checkGoalOwnership(goal, user.id)
        goalRecordStore.deleteAllByGoal(goal)

        goalStore.delete(goal)
    }
}