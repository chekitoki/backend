package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goalrecord.service.GoalRecordStore
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class GoalRecordScheduler(
    private val goalRecordStore: GoalRecordStore,
    private val goalStore: GoalStore,
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun createDailyGoalRecord() {
        val goals = goalStore.findAllByPeriod(GoalPeriod.DAILY)
        goals.forEach { goal ->
            val today = LocalDate.now()
            goalRecordStore.findOrCreate(goal, today)
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    fun createWeeklyGoalRecord() {
        val goals = goalStore.findAllByPeriod(GoalPeriod.WEEKLY)
        goals.forEach { goal ->
            val today = LocalDate.now()
            goalRecordStore.findOrCreate(goal, today)
        }
    }

    @Scheduled(cron = "0 0 0 1 * *")
    fun createMonthlyGoalRecord() {
        val goals = goalStore.findAllByPeriod(GoalPeriod.MONTHLY)
        goals.forEach { goal ->
            val today = LocalDate.now()
            goalRecordStore.findOrCreate(goal, today)
        }
    }
}