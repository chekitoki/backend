package com.example.chekitoki.domain.goal.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.goal.dto.GoalRequestDto
import com.example.chekitoki.domain.goal.dto.GoalResponseDto
import com.example.chekitoki.domain.goal.service.GoalService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/goal")
class GoalController(
    private val goalService: GoalService,
) {
    @PostMapping
    fun createGoal(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: GoalRequestDto.Create,
    ): GoalResponseDto {
        val response = goalService.createGoal(userDetails.user, request.toInfo())
        return response.toResponseDetail()
    }

    @PostMapping("/list")
    fun getGoals(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: GoalRequestDto.Read,
    ): List<GoalResponseDto> {
        val response = goalService.getGoals(userDetails.user, request.toInfo())
        return response.map { it.toResponseWithRecord() }
    }

    @PatchMapping
    fun updateGoal(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: GoalRequestDto.Update,
    ): GoalResponseDto {
        val response = goalService.updateGoal(userDetails.user, request.toInfo())
        return response.toResponseDetail()
    }

    @DeleteMapping("/{goalId}")
    fun deleteGoal(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable goalId: Long,
    ) {
        goalService.deleteGoal(userDetails.user, goalId)
    }
}