package com.example.chekitoki.domain.goal.model

import com.example.chekitoki.domain.BaseEntity
import com.example.chekitoki.domain.goal.enum.GoalPeriod
import com.example.chekitoki.domain.goal.enum.GoalStatus
import com.example.chekitoki.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "goal")
@SQLDelete(sql = "UPDATE goal SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class Goal(
    user: User,
    title: String,
    description: String?,
    target: Int,
    unit: String,
    period: GoalPeriod,
) : BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User = user

    var title: String = title

    var description: String? = description

    var target: Int = target

    var unit: String = unit

    @Enumerated(EnumType.STRING)
    val period: GoalPeriod = period

    var status: GoalStatus = GoalStatus.ACTIVE

    var deletedAt: LocalDateTime? = null

    fun updateGoal(title: String?, description: String?, target: Int?, unit: String?) {
        title?.let { this.title = it }
        description?.let { this.description = it }
        target?.let { this.target = it }
        unit?.let { this.unit = it }
    }

    fun updateStatus(status: GoalStatus) {
        this.status = status
    }
}