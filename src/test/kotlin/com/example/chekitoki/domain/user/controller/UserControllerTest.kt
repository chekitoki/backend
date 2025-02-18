package com.example.chekitoki.domain.user.controller

import com.example.chekitoki.config.auth.CustomUserDetails
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.user.exception.DuplicatePasswordException
import com.example.chekitoki.domain.user.exception.DuplicateUserException
import com.example.chekitoki.domain.user.exception.NoSuchUserException
import com.example.chekitoki.domain.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean val userService: UserService,
) : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    beforeContainer {
        val userDetails = CustomUserDetails(UserFixtures.testUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    describe("새로운 유저 정보 생성") {
        context("올바른 정보로 유저를 생성할 경우") {
            every { userService.createUser(any()) } returns UserFixtures.UserResponseInfo
            it("생성한 유저의 정보를 담은 200 상태 코드 반환") {
                mockMvc.post("/api/user") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.createRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.id") { value(UserFixtures.id) }
                        jsonPath("$.userId") { value(UserFixtures.userId)}
                        jsonPath("$.name") { value(UserFixtures.userName) }
                        jsonPath("$.email") { value(UserFixtures.userEmail) }
                    }
                }
            }
        }

        context("이미 존재하는 아이디 정보로 유저를 생성할 경우") {
            every { userService.createUser(any()) } throws DuplicateUserException(null)
            it("409 상태 코드 반환") {
                mockMvc.post("/api/user") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.createRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(409) }
                    }
                }
            }
        }

        context("올바르지 않은 양식의 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/user") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.wrongCreateRequest)
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                    }
                }
            }
        }

        context("요구하는 필드가 누락된 경우") {
            it("400 상태 코드 반환") {
                mockMvc.post("/api/user") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.createRequest.copy(email = ""))
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

    describe("유저 정보 조회") {
        context("존재하는 유저의 정보를 조회할 경우") {
            every { userService.getUser(any()) } returns UserFixtures.UserResponseInfo
            it("유저 정보를 조회하고 200 상태 코드 반환") {
                mockMvc.get("/api/user/${UserFixtures.id}")
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
        context("존재하지 않는 유저의 정보를 조회할 경우") {
            every { userService.getUser(any()) } throws NoSuchUserException(null)
            it("404 상태 코드 반환") {
                mockMvc.get("/api/user/999")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        content {
                            jsonPath("$.code") { value(404) }
                        }
                    }
            }
        }
    }

    describe("유저 프로필 업데이트") {
        context("올바른 정보로 유저 프로필을 업데이트할 경우") {
            every { userService.updateProfile(any(), any()) } returns UserFixtures.UserUpdatedResponseInfo
            it("유저 프로필을 업데이트하고 200 상태 코드 반환") {
                mockMvc.patch("/api/user/update/profile") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.updateProfileRequest)
                }.andDo{print()}
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        content {
                            jsonPath("$.id") { value(UserFixtures.id) }
                            jsonPath("$.userId") { value(UserFixtures.userId) }
                            jsonPath("$.name") { value(UserFixtures.modifiedName) }
                            jsonPath("$.email") { value(UserFixtures.userEmail) }
                        }
                    }
            }
        }

        context("올바르지 않은 양식의 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/user/update/profile") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.updateProfileRequest.copy(name = ""))
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

    describe("유저 비밀번호 업데이트") {
        context("올바른 정보로 유저 비밀번호를 업데이트할 경우") {
            every { userService.updatePassword(any(), any()) } just Runs
            it("유저 비밀번호를 업데이트하고 200 상태 코드 반환") {
                mockMvc.patch("/api/user/update/password") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.updatePasswordRequest)
                }.andExpect {
                    status { isOk() }
                }
            }
        }

        context("올바르지 않은 양식의 요청을 보낼 경우") {
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/user/update/password") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.wrongUpdatePasswordRequest)
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                    }
                }
            }
        }

        context("이전 비밀번호와 새로운 비밀번호가 같을 경우") {
            every { userService.updatePassword(any(), any()) } throws DuplicatePasswordException(null)
            it("400 상태 코드 반환") {
                mockMvc.patch("/api/user/update/password") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(UserFixtures.duplicatePasswordRequest)
                }.andDo { print() }
                    .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        jsonPath("$.code") { value(400) }
                    }
                }
            }
        }
    }

    describe("유저 삭제") {
        every { userService.deleteUser(any()) } just Runs
        it("유저를 삭제하고 200 상태 코드 반환") {
            mockMvc.delete("/api/user") {
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }
        }
    }
})