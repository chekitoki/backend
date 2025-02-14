package com.example.chekitoki.domain.goalrecord.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

class GoalRecordRequestDto {
    // TODO: !!를 사용하지 않고, NotNull을 처리할 수 있는 방안 모색
    data class Create(
        @field:NotNull(message = "목표 ID는 필수 값입니다.")
        val goalId: Long?,
        @field:NotNull(message = "날짜는 필수 값입니다.")
        val date: LocalDate?,
    ) {
        fun toInfo() = GoalRecordInfo.Create(
            goalId = goalId!!,
            date = date!!,
        )
    }

    data class Update(
        @field:NotNull(message = "ID는 필수 값입니다.")
        val id: Long?,
        @field:NotNull(message = "달성도는 필수 값입니다.")
        val achievement: Int?,
    ) {
        fun toInfo() = GoalRecordInfo.Update(
            id = id!!,
            achievement = achievement!!,
        )
    }
}