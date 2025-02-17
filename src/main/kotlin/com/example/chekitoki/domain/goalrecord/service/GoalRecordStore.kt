package com.example.chekitoki.domain.goalrecord.service

import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import java.time.LocalDate

interface GoalRecordStore {
    fun save(goalRecord: GoalRecord): GoalRecord
    fun findOrCreate(goal: Goal, date: LocalDate?): GoalRecord
    fun findAllByGoal(goal: Goal): List<GoalRecord>
    fun findByGoalAndDate(goal: Goal, date: LocalDate): GoalRecord?
    fun getById(id: Long): GoalRecord
    fun deleteAllByGoal(goal: Goal)
    fun delete(goalRecord: GoalRecord)
}