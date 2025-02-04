package com.example.chekitoki.domain.user.service

import com.example.chekitoki.config.PasswordEncoderWrapper
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.user.exception.NoSuchUserException
import com.example.chekitoki.domain.user.repository.UserRepository
import com.example.chekitoki.domain.user.repository.UserStoreImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.util.*

class UserServiceTest : DescribeSpec({
    val userRepository: UserRepository = mockk<UserRepository>()
    val userStore: UserStore = UserStoreImpl(userRepository)
    val encoder: PasswordEncoderWrapper = mockk()
    val userService: UserService = UserService(userStore, encoder)

    describe("createUser") {
        beforeEach { every { encoder.encode(any()) } returns "encodedPassword" }
        context("정상적인 회원 가입 요청이 주어진 경우") {
            it("새로운 유저를 성공적으로 생성한다.") {
                every { userRepository.findById(UserFixtures.id) } returns Optional.of(UserFixtures.testUser)

                val result = userService.createUser(UserFixtures.CreateInfo)

                result.userId shouldBe UserFixtures.userId
                result.name shouldBe UserFixtures.userName
                result.email shouldBe UserFixtures.userEmail
            }
        }

        context("이미 존재하는 아이디로 회원가입을 시도하면") {
            it("중복 유저 예외를 던진다.") {
                every { userRepository.existsByUserId(UserFixtures.userId) } returns true

                shouldThrow<DuplicateUserException> {
                    userService.createUser(UserFixtures.CreateInfo)
                }
            }
        }
    }

    describe("getUser") {
        context("존재하는 유저에 대한 정보를 요청하면") {
            it("유저 정보를 반환한다.") {
                every { userRepository.findById(UserFixtures.id) } returns Optional.of(UserFixtures.testUser)

                val result = userService.getUser(UserFixtures.id)

                result.userId shouldBe UserFixtures.userId
            }
        }

        context("존재하지 않는 유저에 대한 정보를 요청하면") {
            it("유저 정보가 존재하지 않는다는 예외를 던진다.") {
                every { userRepository.findById(UserFixtures.id) } returns Optional.empty()

                shouldThrow<NoSuchUserException> {
                    userService.getUser(UserFixtures.id)
                }
            }
        }
    }

    describe("updateProfile") {
        context("정상적인 프로필 수정 요청이 주어진 경우") {
            it("유저의 프로필을 수정한다.") {
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser

                val result = userService.updateProfile(UserFixtures.userId, UserFixtures.UpdateProfileInfo)

                result.name shouldBe UserFixtures.modifiedName
            }
        }
    }

    describe("updatePassword") {
        context("정상적인 비밀번호 수정 요청이 주어진 경우") {
            it("유저의 비밀번호를 수정한다.") {
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser

                userService.updatePassword(UserFixtures.userId, UserFixtures.UpdatePasswordInfo)
            }
        }

        context("유저가 입력한 이전 비밀번호가 일치하지 않으면") {
            it("비밃번호 불일치 예외를 던진다.") {
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser

                shouldThrow<InvalidPasswordException> {
                    userService.updatePassword(UserFixtures.userId, UserFixtures.WrongUpdatePasswordInfo)
                }
            }
        }

        context("유저가 입력한 새로운 비밀번호가 이전 비밀번호와 동일하면") {
            it("새로운 비밀번호가 이전 비밀번호와 동일하다는 예외를 던진다.") {
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser

                shouldThrow<DuplicatePasswordException> {
                    userService.updatePassword(UserFixtures.userId, UserFixtures.DuplicatePasswordInfo)
                }
            }
        }
    }

    describe("deleteUser") {
        context("정상적인 회원 탈퇴 요청이 주어진 경우") {
            it("유저를 삭제한다.") {
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser

                userService.deleteUser(UserFixtures.userId)
            }
        }
    }
})
