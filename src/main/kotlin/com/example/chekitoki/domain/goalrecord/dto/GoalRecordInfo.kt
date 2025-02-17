package com.example.chekitoki.domain.goalrecord.dto

import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import java.time.LocalDate

class GoalRecordInfo {
    data class Create(
        val goalId: Long,
        val date: LocalDate,
    )

    data class Update(
        val id: Long,
        val achievement: Int,
    )

    data class Response(
        val id: Long,
        val date: LocalDate,
        val achievement: Int,
    ) {
        constructor(goalRecord: GoalRecord): this(
            id = goalRecord.id,
            date = goalRecord.date,
            achievement = goalRecord.achievement,
        )

        fun toResponseDto() = GoalRecordResponseDto(
            id = id,
            date = date,
            achievement = achievement,
        )
    }
}