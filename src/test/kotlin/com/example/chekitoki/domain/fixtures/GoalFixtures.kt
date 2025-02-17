package com.example.chekitoki.domain.fixtures

import com.example.chekitoki.domain.goal.dto.GoalInfo
import com.example.chekitoki.domain.goal.dto.GoalRequestDto
import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal
import java.time.LocalDate

class GoalFixtures {
    companion object {
        const val dailyGoalId = 1L
        private const val dailyGoalTitle = "물 1.5L 마시기"
        private const val dailyGoalDescription = "텀블러에 750ml씩 두 번 마시기"
        private const val dailyGoalTarget = 1500
        private const val dailyGoalUnit = "ml"
        private val dailyGoalType = GoalPeriod.DAILY

        private const val weeklyGoalTitle = "코딩테스트 문제 풀기"
        private const val weeklyGoalDescription = "백준 5문제 풀기"
        private const val weeklyGoalTarget = 5
        private const val weeklyGoalUnit = "문제"
        private val weeklyGoalType = GoalPeriod.WEEKLY

        private const val monthlyGoalTitle = "책 읽기"
        private const val monthlyGoalTarget = 2
        private const val monthlyGoalUnit = "권"
        private val monthlyGoalType = GoalPeriod.MONTHLY

        private const val dailyModifiedGoalTitle = "물 2L 마시기"
        private const val dailyModifiedGoalDescription = "텀블러에 1L씩 두 번 마시기"
        private const val dailyModifiedGoalTarget = 2
        private const val dailyModifiedGoalUnit = "L"

        private val user = UserFixtures.testUser
        val dailyGoal = Goal(user, dailyGoalTitle, dailyGoalDescription, dailyGoalTarget, dailyGoalUnit, dailyGoalType)
        val weeklyGoal = Goal(user, weeklyGoalTitle, weeklyGoalDescription, weeklyGoalTarget, weeklyGoalUnit, weeklyGoalType)
        val monthlyGoal = Goal(user, monthlyGoalTitle, "", monthlyGoalTarget, monthlyGoalUnit, monthlyGoalType)
        val modifiedDailyGoal = Goal(user, dailyModifiedGoalTitle, dailyModifiedGoalDescription, dailyModifiedGoalTarget, dailyModifiedGoalUnit, dailyGoalType)

        /* request dto */
        val createGoalRequest = GoalRequestDto.Create(dailyGoalTitle, dailyGoalDescription, dailyGoalTarget, dailyGoalUnit, dailyGoalType)
        val readGoalRequest = GoalRequestDto.Read(GoalPeriod.DAILY, LocalDate.now())
        val readGoalWithoutDateRequest = GoalRequestDto.Read(GoalPeriod.DAILY, null)
        val updateGoalRequest = GoalRequestDto.Update(dailyGoalId, dailyModifiedGoalTitle, dailyModifiedGoalDescription, dailyModifiedGoalTarget, dailyModifiedGoalUnit)

        /* info */
        val createGoalInfo = createGoalRequest.toInfo()
        val readGoalInfo = readGoalRequest.toInfo()
        val readGoalWithoutDateInfo = readGoalWithoutDateRequest.toInfo()
        val updateGoalInfo = updateGoalRequest.toInfo()
        val dailyGoalResponseInfo = GoalInfo.Response(dailyGoal)
        val dailyGoalWithRecordResponseInfo = GoalInfo.ResponseWithRecord(dailyGoal, emptyList())

        /* response */
        val dailyGoalResponseDetail = dailyGoalResponseInfo.toResponseDetail()
        val dailyGoalResponseSummary = dailyGoalResponseInfo.toResponseSummary()
        val dailyGoalWithRecordResponseDetail = dailyGoalWithRecordResponseInfo.toResponseWithRecord()
    }
}