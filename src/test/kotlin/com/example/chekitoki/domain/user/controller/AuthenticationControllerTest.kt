package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.user.exception.InvalidCredentialsException
import com.example.chekitoki.domain.user.service.AuthenticationService
import com.example.chekitoki.utils.CookieUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.runs
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
class AuthenticationControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean val authenticationService: AuthenticationService,
) : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    describe("로그인 요청") {
        context("올바른 양식으로 로그인 요청을 보낼 경우") {
            every { authenticationService.login(any()) } returns UserFixtures.UserResponseInfo
            it("로그인 성공 후 유저 정보를 반환") {
                mockMvc.post("/api/auth/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.loginRequest)
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.id") { value(UserFixtures.id) }
                        jsonPath("$.userId") { value(UserFixtures.userId) }
                        jsonPath("$.name") { value(UserFixtures.userName) }
                        jsonPath("$.email") { value(UserFixtures.userEmail) }
                    }
                }
            }
        }
        context("올바르지 않은 양식으로 로그인 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/auth/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        mapOf("userId" to "test")
                    )
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                    }
                }
            }
        }
        context("잘못된 정보로 로그인 요청을 보낼 경우") {
            every { authenticationService.login(any()) } throws InvalidCredentialsException("")
            it("401 상태 코드 반환") {
                mockMvc.post("/api/auth/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.wrongLoginRequest)
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(401) }
                    }
                }
            }
        }
    }

    describe("로그아웃 요청") {
        context("로그인 상태로 로그아웃 요청을 보낼 경우") {
            beforeTest {
                val userDetails = CustomUserDetails(UserFixtures.testUser)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
            it("로그아웃 성공 후 200 상태 코드 반환") {
                mockMvc.delete("/api/auth/logout") {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                }
            }
        }

        context("로그인 상태가 아닌 상태로 로그아웃 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.delete("/api/auth/logout") {
                    contentType = MediaType.APPLICATION_JSON
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

    describe("토큰 재발급 요청") {
        context("리프레시 토큰을 가지고 재발급 요청을 보낼 경우") {
            every { authenticationService.reissue() } just runs
            it("재발급 성공 후 200 상태 코드 반환") {
                mockMvc.patch("/api/auth/reissue") {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                }
            }
        }
        context("리프레시 토큰이 없는 상태로 재발급 요청을 보낼 경우") {
            every { authenticationService.reissue() } throws CookieUtils.NoSuchCookieException(null)
            it("404 상태 코드 반환") {
                mockMvc.patch("/api/auth/reissue") {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(404) }
                    }
                }
            }
        }
    }
})
