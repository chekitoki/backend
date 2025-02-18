package com.example.chekitoki.domain.goal.dto

import com.example.chekitoki.domain.goal.enum.GoalPeriod
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

class GoalRequestDto {
    // TODO: !!를 사용하지 않고, NotBlank를 처리할 수 있는 방안 모색
    companion object {
        const val ID_NOT_NULL_MESSAGE = "ID를 입력해주세요."
        const val TITLE_NOT_BLANK_MESSAGE = "목표명을 입력해주세요."
        const val TITLE_LENGTH_MESSAGE = "목표명은 20자 이내로 입력해주세요."
        const val DESCRIPTION_LENGTH_MESSAGE = "설명은 100자 이내로 입력해주세요."
        const val TARGET_NOT_NULL_MESSAGE = "목표치를 입력해주세요."
        const val UNIT_NOT_BLANK_MESSAGE = "단위를 입력해주세요."
        const val UNIT_LENGTH_MESSAGE = "단위는 10자 이내로 입력해주세요."
        const val PERIOD_NOT_NULL_MESSAGE = "주기를 입력해주세요."
    }
    data class Create(
        @field:NotBlank(message = TITLE_NOT_BLANK_MESSAGE)
        @field:Length(max = 20, message = TITLE_LENGTH_MESSAGE)
        val title: String?,
        @field:Length(max = 100, message = DESCRIPTION_LENGTH_MESSAGE)
        val description: String?,
        @field:NotNull(message = TARGET_NOT_NULL_MESSAGE)
        val target: Int?,
        @field:NotBlank(message = UNIT_NOT_BLANK_MESSAGE)
        @field:Length(max = 10, message = UNIT_LENGTH_MESSAGE)
        val unit: String?,
        @field:NotNull(message = PERIOD_NOT_NULL_MESSAGE)
        val period: GoalPeriod?,
    ) {
        fun toInfo() = GoalInfo.Create(
            title = title!!,
            description = description,
            target = target!!,
            unit = unit!!,
            period = period!!,
        )
    }

    data class Read(
        @field:NotNull(message = PERIOD_NOT_NULL_MESSAGE)
        val period: GoalPeriod?,
        val date: LocalDate?,
    ) {
        fun toInfo() = GoalInfo.Read(
            period = period!!,
            date = date,
        )
    }

    data class Update(
        @field:NotNull(message = ID_NOT_NULL_MESSAGE)
        val id: Long,
        @field:NotBlank(message = TITLE_NOT_BLANK_MESSAGE)
        @field:Length(max = 20, message = TITLE_LENGTH_MESSAGE)
        val title: String?,
        @field:Length(max = 100, message = DESCRIPTION_LENGTH_MESSAGE)
        val description: String?,
        @field:NotNull(message = TARGET_NOT_NULL_MESSAGE)
        val target: Int?,
        @field:NotBlank(message = UNIT_NOT_BLANK_MESSAGE)
        @field:Length(max = 10, message = UNIT_LENGTH_MESSAGE)
        val unit: String?,
    ) {
        fun toInfo() = GoalInfo.Update(
            id = id,
            title = title,
            description = description,
            target = target,
            unit = unit,
        )
    }
}