package com.example.chekitoki.domain.goalrecord.service

import com.example.chekitoki.domain.goal.service.GoalStore
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalRecordService(
    private val goalRecordStore: GoalRecordStore,
    private val goalStore: GoalStore,
) {
    @Transactional
    fun createGoalRecord(userId: String, info: GoalRecordInfo.Create): GoalRecordInfo.Response {
        val goal = goalStore.getById(info.goalId)

        goalStore.checkGoalOwnership(goal, userId)

        return GoalRecordInfo.Response(goalRecordStore.findOrCreate(goal, info.date))
    }

    @Transactional
    fun updateGoalRecord(userId: String, info: GoalRecordInfo.Update): GoalRecordInfo.Response {
        val goalRecord = goalRecordStore.getById(info.id)

        goalStore.checkGoalOwnership(goalRecord.goal, userId)

        goalRecord.updateGoalRecord(info.achievement)
        return GoalRecordInfo.Response(goalRecordStore.save(goalRecord))
    }
}