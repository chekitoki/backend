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
        private val goalLastWeek: LocalDate = LocalDate.now().minusWeeks(1)
        private val goalLastMonth: LocalDate = LocalDate.now().minusMonths(1)
        private const val modifiedAchievement = 1000

        private val dailyGoal = GoalFixtures.dailyGoal
        private val weeklyGoal = GoalFixtures.weeklyGoal
        private val monthlyGoal = GoalFixtures.monthlyGoal

        val dailyGoalRecord = GoalRecord(dailyGoal, goalToday)
        private val dailyGoalRecordYesterday = GoalRecord(dailyGoal, goalYesterday)
        private val weeklyGoalRecord = GoalRecord(weeklyGoal, goalLastWeek)
        private val weeklyGoalRecordLastWeek = GoalRecord(weeklyGoal, goalLastWeek)
        private val monthlyGoalRecord = GoalRecord(monthlyGoal, goalLastMonth)
        private val monthlyGoalRecordLastMonth = GoalRecord(monthlyGoal, goalLastMonth)

        val modifiedDailyGoalRecord = GoalRecord(dailyGoal, goalToday).apply { achievement = modifiedAchievement }

        val dailyGoalRecords = listOf<GoalRecord>(dailyGoalRecord, dailyGoalRecordYesterday)
        val weeklyGoalRecords = listOf<GoalRecord>(weeklyGoalRecord, weeklyGoalRecordLastWeek)
        val monthlyGoalRecords = listOf<GoalRecord>(monthlyGoalRecord, monthlyGoalRecordLastMonth)

        /* request dto */
        val createGoalRecordRequest = GoalRecordRequestDto.Create(GoalFixtures.dailyGoalId, goalToday)
        val updateGoalRecordRequest = GoalRecordRequestDto.Update(dailyGoalRecordId, modifiedAchievement)

        /* info */
        val createGoalRecordInfo = createGoalRecordRequest.toInfo()
        val updateGoalRecordInfo = updateGoalRecordRequest.toInfo()
        val dailyGoalRecordResponseInfo = GoalRecordInfo.Response(dailyGoalRecord)

        /* response */
        val dailyGoalRecordResponseDetail = dailyGoalRecordResponseInfo.toResponseDto()
    }
}