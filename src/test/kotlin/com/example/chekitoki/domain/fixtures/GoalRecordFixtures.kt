package com.example.chekitoki.domain.fixtures

import com.example.chekitoki.domain.goalrecord.dto.GoalRecordInfo
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordRequestDto
import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import java.time.LocalDate

class GoalRecordFixtures {
     companion object {
        const val dailyGoalRecordId = 1L
        private val goalToday: LocalDate = LocalDate.now()
        private val goalYesterday: LocalDate = LocalDate.now().minusDays(1)
        private const val modifiedAchievement = 1000

        val dailyGoalRecord = GoalRecord(GoalFixtures.dailyGoal, goalToday)
        private val dailyGoalRecordYesterday = GoalRecord(GoalFixtures.dailyGoal, goalYesterday)

        val modifiedDailyGoalRecord = GoalRecord(GoalFixtures.dailyGoal, goalToday).apply { achievement = modifiedAchievement }

        val dailyGoalRecords = listOf<GoalRecord>(dailyGoalRecord, dailyGoalRecordYesterday)

        /* request dto */
        val createGoalRecordRequest = GoalRecordRequestDto.Create(GoalFixtures.dailyGoalId, goalToday)
        val updateGoalRecordRequest = GoalRecordRequestDto.Update(dailyGoalRecordId, modifiedAchievement)

        /* info */
        val createGoalRecordInfo = createGoalRecordRequest.toInfo()
        val updateGoalRecordInfo = updateGoalRecordRequest.toInfo()
        val dailyGoalRecordResponseInfo = GoalRecordInfo.Response(dailyGoalRecord)
        val dailyGoalRecordsResponseInfo = dailyGoalRecords.map { GoalRecordInfo.Response(it) }
        val modifiedGoalRecordResponseInfo = GoalRecordInfo.Response(modifiedDailyGoalRecord)
    }
}