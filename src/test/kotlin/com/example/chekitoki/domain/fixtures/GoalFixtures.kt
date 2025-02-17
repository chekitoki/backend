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

        private const val dailyModifiedGoalTitle = "물 2L 마시기"
        private const val dailyModifiedGoalDescription = "텀블러에 1L씩 두 번 마시기"
        private const val dailyModifiedGoalTarget = 2
        private const val dailyModifiedGoalUnit = "L"

        private const val wrongGoalTitle = "20자를초과한목표타이틀은생성이허용되지않습니다"
        private var wrongGoalDescription = "100자를초과한목표설명은생성이허용되지않습니다".repeat(5)
        private const val wrongGoalUnit = "10를초과한단위는불가능"

        private val user = UserFixtures.testUser
        val dailyGoal = Goal(user, dailyGoalTitle, dailyGoalDescription, dailyGoalTarget, dailyGoalUnit, dailyGoalType)
        val modifiedDailyGoal = Goal(user, dailyModifiedGoalTitle, dailyModifiedGoalDescription, dailyModifiedGoalTarget, dailyModifiedGoalUnit, dailyGoalType)

        /* request dto */
        val createGoalRequest = GoalRequestDto.Create(dailyGoalTitle, dailyGoalDescription, dailyGoalTarget, dailyGoalUnit, dailyGoalType)
        val wrongCreateGoalRequest = GoalRequestDto.Create(wrongGoalTitle, wrongGoalDescription, dailyGoalTarget, wrongGoalUnit, dailyGoalType)
        val readGoalRequest = GoalRequestDto.Read(GoalPeriod.DAILY, LocalDate.now())
        private val readGoalWithoutDateRequest = GoalRequestDto.Read(GoalPeriod.DAILY, null)
        val updateGoalRequest = GoalRequestDto.Update(dailyGoalId, dailyModifiedGoalTitle, dailyModifiedGoalDescription, dailyModifiedGoalTarget, dailyModifiedGoalUnit)
        val wrongUpdateGoalRequest = GoalRequestDto.Update(dailyGoalId, wrongGoalTitle, wrongGoalDescription, dailyGoalTarget, wrongGoalUnit)

        /* info */
        val createGoalInfo = createGoalRequest.toInfo()
        val readGoalInfo = readGoalRequest.toInfo()
        val readGoalWithoutDateInfo = readGoalWithoutDateRequest.toInfo()
        val updateGoalInfo = updateGoalRequest.toInfo()
        val dailyGoalResponseInfo = GoalInfo.Response(dailyGoalId, dailyGoal.title, dailyGoal.description, dailyGoal.target, dailyGoal.unit, dailyGoal.period)
        val dailyGoalWithRecordsResponseInfo by lazy {
            GoalInfo.ResponseWithRecord(dailyGoalId, dailyGoal.title, dailyGoal.description, dailyGoal.target, dailyGoal.unit, dailyGoal.period, GoalRecordFixtures.dailyGoalRecordsResponseInfo)
        }
        val modifiedGoalResponseInfo = GoalInfo.Response(modifiedDailyGoal)
    }
}