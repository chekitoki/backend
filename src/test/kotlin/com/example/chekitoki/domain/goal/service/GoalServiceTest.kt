package com.example.chekitoki.domain.goal.service

import com.example.chekitoki.api.exception.ResourceAuthorizationException
import com.example.chekitoki.domain.fixtures.GoalFixtures
import com.example.chekitoki.domain.fixtures.GoalRecordFixtures
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.goal.exception.NoSuchGoalException
import com.example.chekitoki.domain.goal.repository.GoalRepository
import com.example.chekitoki.domain.goal.repository.GoalStoreImpl
import com.example.chekitoki.domain.goalrecord.repository.GoalRecordRepository
import com.example.chekitoki.domain.goalrecord.repository.GoalRecordStoreImpl
import com.example.chekitoki.domain.goalrecord.service.GoalRecordStore
import com.example.chekitoki.domain.user.repository.UserRepository
import com.example.chekitoki.domain.user.repository.UserStoreImpl
import com.example.chekitoki.domain.user.service.UserStore
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import java.util.*

class GoalServiceTest : DescribeSpec({
    val goalRepository: GoalRepository = mockk<GoalRepository>()
    val goalStore: GoalStore = GoalStoreImpl(goalRepository)
    val goalRecordRepository: GoalRecordRepository = mockk<GoalRecordRepository>()
    val goalRecordStore: GoalRecordStore = GoalRecordStoreImpl(goalRecordRepository)
    val userRepository: UserRepository = mockk<UserRepository>()
    val userStore: UserStore = UserStoreImpl(userRepository)

    val goalService = GoalService(goalStore, goalRecordStore, userStore)

    // fixtures
    val userId = UserFixtures.userId
    val anotherUserId = UserFixtures.anotherUserId
    val user = UserFixtures.testUser
    val dailyGoalId = GoalFixtures.dailyGoalId
    val dailyGoal = GoalFixtures.dailyGoal
    val dailyGoalRecord = GoalRecordFixtures.dailyGoalRecord
    val dailyGoalRecords = GoalRecordFixtures.dailyGoalRecords

    beforeContainer {
        every { userRepository.findByUserId(userId) } returns user
    }

    describe("createGoal") {
        context("정상적인 목표 생성 요청이 주어진 경우") {
            it("새로운 목표를 생성하고 반환한다.") {
                every { goalRepository.save(any()) } returns dailyGoal
                every { goalRecordRepository.findByGoalAndDate(any(), any()) } returns null
                every { goalRecordRepository.save(any()) } returns dailyGoalRecord

                val result = goalService.createGoal(userId, GoalFixtures.createGoalInfo)

                result.id shouldBe dailyGoal.id
                result.title shouldBe dailyGoal.title
                result.description shouldBe dailyGoal.description
                result.target shouldBe dailyGoal.target
                result.unit shouldBe dailyGoal.unit
                result.period shouldBe dailyGoal.period
            }
        }
    }

    describe("getGoals") {
        beforeEach {
            every { goalRepository.findByUserUserIdAndPeriod(userId, any()) } returns listOf(dailyGoal)
        }
        context("date 정보가 주어진 경우") {
            it("해당 날짜에 포함되는 목표와 기록을 반환한다.") {
                every { goalRecordRepository.findByGoalAndDate(any(), any()) } returns dailyGoalRecord

                val result = goalService.getGoals(userId, GoalFixtures.readGoalInfo)

                result[0].id shouldBe dailyGoal.id
                result[0].records.size shouldBe 1
                result[0].records[0].id shouldBe dailyGoalRecord.id
            }
        }
        context("date 정보가 주어지지 않은 경우") {
            it("period 정보에 따른 목표와 기록을 반환한다.") {
                every { goalRecordRepository.findAllByGoal(any()) } returns dailyGoalRecords

                val result = goalService.getGoals(userId, GoalFixtures.readGoalWithoutDateInfo)

                result[0].id shouldBe dailyGoal.id
                result[0].records.size shouldBe 2
            }
        }
    }

    describe("updateGoal") {
        context("정상적인 목표 수정 요청이 주어진 경우") {
            it("목표를 수정하고 반환한다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)
                every { goalRepository.save(any()) } returns GoalFixtures.modifiedDailyGoal

                val result = goalService.updateGoal(userId, GoalFixtures.updateGoalInfo)

                result.id shouldBe GoalFixtures.modifiedDailyGoal.id
                result.title shouldBe GoalFixtures.modifiedDailyGoal.title
                result.description shouldBe GoalFixtures.modifiedDailyGoal.description
                result.target shouldBe GoalFixtures.modifiedDailyGoal.target
                result.unit shouldBe GoalFixtures.modifiedDailyGoal.unit
                result.period shouldBe GoalFixtures.modifiedDailyGoal.period
            }
        }
        context("존재하지 않는 목표를 수정하려고 시도하는 경우") {
            it("존재하지 않는 목표 예외를 던진다.") {
                every { goalRepository.findById(any()) } returns Optional.empty()

                shouldThrow<NoSuchGoalException> {
                    goalService.updateGoal(userId, GoalFixtures.updateGoalInfo)
                }
            }
        }
        context("목표 작성자가 아닌 사용자가 목표를 수정하려고 시도하는 경우") {
            it("리소스에 대한 권한이 없다는 예외를 던진다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)

                shouldThrow<ResourceAuthorizationException> {
                    goalService.updateGoal(anotherUserId, GoalFixtures.updateGoalInfo)
                }
            }
        }
    }

    describe("deleteGoal") {
        context("정상적인 목표 삭제 요청이 주어진 경우") {
            it("목표를 삭제하고 반환한다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)
                every { goalRepository.delete(any()) } just runs
                every { goalRecordRepository.deleteAllByGoal(any()) } just runs

                goalService.deleteGoal(userId, dailyGoalId)
            }
        }
        context("존재하지 않는 목표를 삭제하려고 시도하는 경우") {
            it("존재하지 않는 목표 예외를 던진다.") {
                every { goalRepository.findById(any()) } returns Optional.empty()

                shouldThrow<NoSuchGoalException> {
                    goalService.deleteGoal(userId, dailyGoalId)
                }
            }
        }
        context("목표 작성자가 아닌 사용자가 목표를 삭제하려고 시도하는 경우") {
            it("리소스에 대한 권한이 없다는 예외를 던진다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)
                every { goalRepository.delete(any()) } just runs
                every { goalRecordRepository.deleteAllByGoal(any()) } just runs

                shouldThrow<ResourceAuthorizationException> {
                    goalService.deleteGoal(anotherUserId, dailyGoalId)
                }
            }
        }
    }
})