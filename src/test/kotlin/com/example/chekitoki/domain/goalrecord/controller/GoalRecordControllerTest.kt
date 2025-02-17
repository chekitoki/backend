package com.example.chekitoki.domain.goalrecord.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.fixtures.GoalRecordFixtures
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.goalrecord.service.GoalRecordService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class GoalRecordControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean val goalRecordService: GoalRecordService,
) : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    beforeContainer {
        val userDetails = CustomUserDetails(UserFixtures.testUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    describe("새로운 목표 기록 생성") {
        context("올바른 정보로 목표 기록을 생성할 경우") {
            it("생성한 목표 기록의 정보를 담은 200 상태 코드 반환") {
                every { goalRecordService.createGoalRecord(any(), any()) } returns GoalRecordFixtures.dailyGoalRecordResponseInfo
                val request = GoalRecordFixtures.createGoalRecordRequest
                mockMvc.post("/api/goal-record") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalRecordFixtures.createGoalRecordRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.date") { value(request.date.toString()) }
                        jsonPath("$.achievement") { value(0) }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/goal-record") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalRecordFixtures.createGoalRecordRequest.copy(goalId = null, date = null))
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.goalId") { exists() }
                        jsonPath("$.data.date") { exists() }
                    }
                }
            }
        }
    }

    describe("목표 기록 정보 업데이트") {
        context("올바른 정보로 목표 기록을 업데이트할 경우") {
            it("업데이트한 목표 기록의 정보를 담은 200 상태 코드 반환") {
                every { goalRecordService.updateGoalRecord(any(), any()) } returns GoalRecordFixtures.modifiedGoalRecordResponseInfo
                val request = GoalRecordFixtures.updateGoalRecordRequest
                mockMvc.patch("/api/goal-record") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalRecordFixtures.updateGoalRecordRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.achievement") { value(request.achievement) }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/goal-record") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalRecordFixtures.updateGoalRecordRequest.copy(id = null, achievement = null))
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.id") { exists() }
                        jsonPath("$.data.achievement") { exists() }
                    }
                }
            }
        }
    }
})
