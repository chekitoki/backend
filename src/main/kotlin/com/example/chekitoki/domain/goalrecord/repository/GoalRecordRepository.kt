package com.example.chekitoki.domain.goalrecord.repository

import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface GoalRecordRepository : JpaRepository<GoalRecord, Long>{
    fun findAllByGoal(goal: Goal): List<GoalRecord>
    fun findByGoalAndDate(goal: Goal, date: LocalDate): GoalRecord?
    fun findByGoalAndDateBetween(goal: Goal, start: LocalDate, end: LocalDate): GoalRecord?
}