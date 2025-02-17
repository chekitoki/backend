package com.example.chekitoki.domain.goalrecord.controller

import com.example.chekitoki.domain.goalrecord.dto.GoalRecordRequestDto
import com.example.chekitoki.domain.goalrecord.dto.GoalRecordResponseDto
import com.example.chekitoki.domain.goalrecord.service.GoalRecordService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/goal-record")
class GoalRecordController(
    private val goalRecordService: GoalRecordService,
) {
    @PostMapping
    fun createGoalRecord(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: GoalRecordRequestDto.Create,
    ): GoalRecordResponseDto {
        val response = goalRecordService.createGoalRecord(userDetails.username, request.toInfo())
        return response.toResponseDto()
    }

    @PatchMapping
    fun updateGoalRecord(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: GoalRecordRequestDto.Update,
    ): GoalRecordResponseDto {
        val response = goalRecordService.updateGoalRecord(userDetails.username, request.toInfo())
        return response.toResponseDto()
    }
}