package com.example.chekitoki.domain.goalrecord.service

import com.example.chekitoki.domain.goal.service.GoalStore
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordInfo
import com.example.chekitoki.domain.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalRecordService(
    private val goalRecordStore: GoalRecordStore,
    private val goalStore: GoalStore,
) {
    @Transactional
    fun createGoalRecord(user: User, info: GoalRecordInfo.Create): GoalRecordInfo.Response {
        val goal = goalStore.getById(info.goalId)

        goalStore.checkGoalOwnership(goal, user.id)

        return GoalRecordInfo.Response(goalRecordStore.findOrCreate(goal, info.date))
    }

    @Transactional
    fun updateGoalRecord(user: User, info: GoalRecordInfo.Update): GoalRecordInfo.Response {
        val goalRecord = goalRecordStore.getById(info.id)

        goalStore.checkGoalOwnership(goalRecord.goal, user.id)

        goalRecord.updateGoalRecord(info.achievement)
        return GoalRecordInfo.Response(goalRecordStore.save(goalRecord))
    }
}