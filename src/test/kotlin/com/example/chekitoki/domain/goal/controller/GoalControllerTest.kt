package com.example.chekitoki.domain.goal.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.fixtures.GoalFixtures
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.goal.service.GoalService
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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class GoalControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean val goalService: GoalService,
) : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    beforeContainer {
        val userDetails = CustomUserDetails(UserFixtures.testUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    describe("새로운 목표 생성") {
        context("올바른 정보로 목표를 생성할 경우") {
            it("생성한 목표의 정보를 담은 200 상태 코드 반환") {
                every { goalService.createGoal(any(), any()) } returns GoalFixtures.dailyGoalResponseInfo
                val request = GoalFixtures.createGoalRequest
                mockMvc.post("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.createGoalRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.id") { value(GoalFixtures.dailyGoalId) }
                        jsonPath("$.title") { value(request.title) }
                        jsonPath("$.description") { value(request.description) }
                        jsonPath("$.target") { value(request.target) }
                        jsonPath("$.unit") { value(request.unit) }
                        jsonPath("$.period") { value(request.period.toString()) }
                    }
                }
            }
        }

        context("올바르지 않은 양식의 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.wrongCreateGoalRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.title") { exists() }
                        jsonPath("$.data.description") { exists() }
                        jsonPath("$.data.unit") { exists() }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.createGoalRequest.copy(title = ""))
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.title") { exists() }
                    }
                }
            }
        }
    }

    describe("요청에 따른 목표 조회") {
        context("올바른 양식의 조회 요청을 보낼 경우") {
            it("목표 정보를 조회하고 200 상태 코드 반환") {
                every { goalService.getGoals(any(), any()) } returns listOf(GoalFixtures.dailyGoalWithRecordsResponseInfo)
                val request = GoalFixtures.readGoalRequest
                mockMvc.post("/api/goal/list") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$[0].id") { value(GoalFixtures.dailyGoalId) }
                        jsonPath("$[0].title") { value(GoalFixtures.dailyGoal.title) }
                        jsonPath("$[0].description") { value(GoalFixtures.dailyGoal.description) }
                        jsonPath("$[0].target") { value(GoalFixtures.dailyGoal.target) }
                        jsonPath("$[0].unit") { value(GoalFixtures.dailyGoal.unit) }
                        jsonPath("$[0].period") { value(GoalFixtures.dailyGoal.period.toString()) }
                        jsonPath("$[0].records") { isArray() }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/goal/list") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.readGoalRequest.copy(period = null))
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                    }
                }
            }
        }
    }

    describe("목표 정보 업데이트") {
        context("올바른 양식의 업데이트 요청을 보낼 경우") {
            it("목표 정보를 업데이트하고 200 상태 코드 반환") {
                val request = GoalFixtures.updateGoalRequest
                every { goalService.updateGoal(any(), any()) } returns GoalFixtures.modifiedGoalResponseInfo
                mockMvc.patch("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.title") { value(request.title) }
                        jsonPath("$.description") { value(request.description) }
                        jsonPath("$.target") { value(request.target) }
                        jsonPath("$.unit") { value(request.unit) }
                    }
                }
            }
        }

        context("올바르지 않은 양식의 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.wrongUpdateGoalRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.title") { exists() }
                        jsonPath("$.data.description") { exists() }
                        jsonPath("$.data.unit") { exists() }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/goal") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(GoalFixtures.updateGoalRequest.copy(title = ""))
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                        jsonPath("$.data.title") { exists() }
                    }
                }
            }
        }
    }

    describe("목표 삭제") {
        context("목표 삭제 요청을 보낼 경우") {
            it("목표를 삭제하고 200 상태 코드 반환") {
                mockMvc.delete("/api/goal/${GoalFixtures.dailyGoalId}").andExpect {
                    status { isOk() }
                }
            }
        }
    }
})
