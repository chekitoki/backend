package com.example.chekitoki.domain.goal.dto

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.model.Goal
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordInfo
import com.example.chekitoki.domain.goalrecord.model.GoalRecord
import java.time.LocalDate

class GoalInfo {
    data class Create(
        val title: String,
        val description: String?,
        val target: Int,
        val unit: String,
        val period: GoalPeriod,
    )

    data class Read(
        val period: GoalPeriod,
        val date: LocalDate?,
    )

    data class Update(
        val id: Long,
        val title: String?,
        val description: String?,
        val target: Int?,
        val unit: String?,
        val period: GoalPeriod?,
    )

    data class Response(
        val id: Long,
        val title: String,
        val description: String?,
        val target: Int,
        val unit: String,
        val period: GoalPeriod,
    ) {
        constructor(goal: Goal): this(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            target = goal.target,
            unit = goal.unit,
            period = goal.period,
            )

        fun toResponseDetail() = GoalResponseDto.Detail(
            id = id,
            title = title,
            description = description,
            target = target,
            unit = unit,
            period = period,
        )

        fun toResponseSummary() = GoalResponseDto.Summary(
            id = id,
            title = title,
            description = description,
        )
    }

    data class ResponseWithRecord(
        val id: Long,
        val title: String,
        val description: String?,
        val target: Int,
        val unit: String,
        val period: GoalPeriod,
        val records: List<GoalRecordInfo.Response>,
    ) {
        constructor(goal: Goal, records: List<GoalRecord>): this(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            target = goal.target,
            unit = goal.unit,
            period = goal.period,
            records = records.map { GoalRecordInfo.Response(it) },
        )

        fun toResponseWithRecord() = GoalResponseDto.WithRecord(
            id = id,
            title = title,
            description = description,
            target = target,
            unit = unit,
            period = period,
            records = records.map { it.toResponseDto() },
        )
    }
}