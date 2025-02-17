package com.example.chekitoki.domain.goalrecord.repository

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.exception.NoSuchGoalRecordException
import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import com.example.chekitoki.domain.goalrecord.service.GoalRecordStore
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class GoalRecordStoreImpl(
    private val goalRecordRepository: GoalRecordRepository,
) : GoalRecordStore {
    override fun save(goalRecord: GoalRecord): GoalRecord {
        return goalRecordRepository.save(goalRecord)
    }

    override fun findOrCreate(goal: Goal, date: LocalDate?): GoalRecord {
        val targetDate = date ?: LocalDate.now()
        val foundGoalRecord = findByPeriod(goal, targetDate)
        return foundGoalRecord ?: save(GoalRecord(goal, targetDate))
    }

    override fun findAllByGoal(goal: Goal): List<GoalRecord> {
        return goalRecordRepository.findAllByGoal(goal)
    }

    override fun findByGoalAndDate(goal: Goal, date: LocalDate): GoalRecord? {
        return findByPeriod(goal, date)
    }

    override fun getById(id: Long): GoalRecord {
        return goalRecordRepository.findById(id)
            .orElseThrow { throw NoSuchGoalRecordException("해당 목표 기록을 찾을 수 없습니다.") }
    }

    override fun delete(goalRecord: GoalRecord) {
        goalRecordRepository.delete(goalRecord)
    }

    private fun findByPeriod(goal: Goal, date: LocalDate): GoalRecord? {
        return when (goal.period) {
            GoalPeriod.DAILY -> goalRecordRepository.findByGoalAndDate(goal, date)
            GoalPeriod.WEEKLY -> {
                val startOfWeek = date.with(DayOfWeek.MONDAY)
                val endOfWeek = date.with(DayOfWeek.SUNDAY)
                goalRecordRepository.findByGoalAndDateBetween(goal, startOfWeek, endOfWeek)
            }
            GoalPeriod.MONTHLY -> {
                val startOfMonth = date.withDayOfMonth(1)
                val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
                goalRecordRepository.findByGoalAndDateBetween(goal, startOfMonth, endOfMonth)
            }
        }
    }
}