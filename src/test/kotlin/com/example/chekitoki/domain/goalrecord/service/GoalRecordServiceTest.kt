package com.example.chekitoki.domain.goalrecord.service

import com.example.chekitoki.api.exception.ResourceAuthorizationException
import com.example.chekitoki.domain.fixtures.GoalFixtures
import com.example.chekitoki.domain.fixtures.GoalRecordFixtures
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.goal.exception.NoSuchGoalException
import com.example.chekitoki.domain.goal.repository.GoalRepository
import com.example.chekitoki.domain.goal.repository.GoalStoreImpl
import com.example.chekitoki.domain.goal.service.GoalStore
import com.example.chekitoki.domain.goalrecord.exception.NoSuchGoalRecordException
import com.example.chekitoki.domain.goalrecord.repository.GoalRecordRepository
import com.example.chekitoki.domain.goalrecord.repository.GoalRecordStoreImpl
import com.example.chekitoki.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

class GoalRecordServiceTest : DescribeSpec({
    val goalRecordRepository: GoalRecordRepository = mockk<GoalRecordRepository>()
    val goalRecordStore: GoalRecordStore = GoalRecordStoreImpl(goalRecordRepository)
    val goalRepository: GoalRepository = mockk<GoalRepository>()
    val goalStore: GoalStore = GoalStoreImpl(goalRepository)
    val userRepository: UserRepository = mockk<UserRepository>()

    val goalRecordService = GoalRecordService(goalRecordStore, goalStore)

    val userId = UserFixtures.userId
    val user = UserFixtures.testUser
    val anotherId = UserFixtures.anotherUserId
    val dailyGoalId = GoalFixtures.dailyGoalId
    val dailyGoal = GoalFixtures.dailyGoal
    val dailyGoalRecordId = GoalRecordFixtures.dailyGoalRecordId
    val dailyGoalRecord = GoalRecordFixtures.dailyGoalRecord

    beforeContainer {
        every { userRepository.findByUserId(userId) } returns user
    }

    describe("createGoalRecord") {
        context("정상적인 목표 기록 생성 요청이 주어진 경우") {
            it("Date에 해당하는 목표가 존재하지 않는 경우 생성 후 반환, 존재하는 경우 반환한다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)
                every { goalRecordRepository.findByGoalAndDate(any(), any()) } returns null
                every { goalRecordRepository.save(any()) } returns dailyGoalRecord

                val result = goalRecordService.createGoalRecord(userId, GoalRecordFixtures.createGoalRecordInfo)

                result.date shouldBe GoalRecordFixtures.createGoalRecordInfo.date
                result.achievement shouldBe 0
            }
        }
        context("존재하지 않는 목표에 대한 목표 기록 생성 요청이 주어진 경우") {
            it("존재하지 않는 목표 예외를 던진다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.empty()

                shouldThrow<NoSuchGoalException> {
                    goalRecordService.createGoalRecord(userId, GoalRecordFixtures.createGoalRecordInfo)
                }
            }
        }
        context("목표 작성자가 아닌 사용자가 목표 기록을 생성하려고 시도하는 경우") {
            it("리소스에 대한 권한이 없다는 예외를 던진다.") {
                every { goalRepository.findById(dailyGoalId) } returns Optional.of(dailyGoal)

                shouldThrow<ResourceAuthorizationException> {
                    goalRecordService.createGoalRecord(anotherId, GoalRecordFixtures.createGoalRecordInfo)
                }
            }
        }
    }

    describe("updateGoalRecord") {
        context("정상적인 목표 기록 수정 요청이 주어진 경우") {
            it("목표 기록을 수정하고 반환한다.") {
                every { goalRecordRepository.findById(dailyGoalRecordId) } returns Optional.of(dailyGoalRecord)
                every { goalRecordRepository.save(any()) } returns GoalRecordFixtures.modifiedDailyGoalRecord

                val result = goalRecordService.updateGoalRecord(userId, GoalRecordFixtures.updateGoalRecordInfo)

                result.date shouldBe GoalRecordFixtures.modifiedDailyGoalRecord.date
                result.achievement shouldBe GoalRecordFixtures.updateGoalRecordInfo.achievement
            }
        }
        context("존재하지 않는 목표 기록을 수정하려고 시도하는 경우") {
            it("존재하지 않는 목표 기록 예외를 던진다.") {
                every { goalRecordRepository.findById(dailyGoalRecordId) } returns Optional.empty()

                shouldThrow<NoSuchGoalRecordException> {
                    goalRecordService.updateGoalRecord(userId, GoalRecordFixtures.updateGoalRecordInfo)
                }
            }
        }
        context("목표 기록 작성자가 아닌 사용자가 목표 기록을 수정하려고 시도하는 경우") {
            it("리소스에 대한 권한이 없다는 예외를 던진다.") {
                every { goalRecordRepository.findById(dailyGoalRecordId) } returns Optional.of(dailyGoalRecord)

                shouldThrow<ResourceAuthorizationException> {
                    goalRecordService.updateGoalRecord(anotherId, GoalRecordFixtures.updateGoalRecordInfo)
                }
            }
        }
    }
})
