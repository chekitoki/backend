package com.example.chekitoki.domain.goalrecord.dto

import java.time.LocalDate

class GoalRecordResponseDto(
    val id: Long,
    val date: LocalDate,
    val achievement: Int,
) {
}