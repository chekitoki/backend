package com.example.chekitoki.domain.goalrecord.model

import com.example.chekitoki.domain.BaseEntity
import com.example.chekitoki.domain.goal.model.Goal
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "goal_record")
@SQLDelete(sql = "UPDATE goal_record SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class GoalRecord(
    goal: Goal,
    date: LocalDate,
) : BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val date: LocalDate = date

    @ManyToOne(fetch = FetchType.LAZY)
    val goal: Goal = goal

    var achievement: Int = 0

    var deletedAt: LocalDateTime? = null

    fun updateGoalRecord(achievement: Int) {
        this.achievement = achievement
    }
}