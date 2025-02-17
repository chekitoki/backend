package com.example.chekitoki.domain.goal.dto

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordResponseDto

sealed class GoalResponseDto {
    data class Detail(
        val id: Long,
        val title: String,
        val description: String?,
        val target: Int,
        val unit: String,
        val period: GoalPeriod,
    ) : GoalResponseDto()

    data class Summary (
        val id: Long,
        val title: String,
        val description: String?,
    ) : GoalResponseDto()

    data class WithRecord(
        val id: Long,
        val title: String,
        val description: String?,
        val target: Int,
        val unit: String,
        val period: GoalPeriod,
        val records: List<GoalRecordResponseDto>,
    ) : GoalResponseDto()
}