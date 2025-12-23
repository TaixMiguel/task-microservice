package com.taixmiguel.penkatur.tasksmanager.application.dto

import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.time.temporal.ChronoUnit

data class TaskRequestDTO(
    var userID: String? = null,
    @field:NotBlank(message = "Task title cannot be blank.")
    val title: String = "",
    @field:NotNull(message = "Task type cannot be null.")
    val type: TaskType? = null,
    @field:NotNull(message = "Task due date cannot be null.")
    val dueDate: Instant? = null
) {
    fun toTask(userID: String): Task {
        return Task(
            title = this.title,
            type = this.type!!,
            userID = this.userID?:userID,
            dueDate = this.dueDate!!.truncatedTo(ChronoUnit.SECONDS)
        )
    }
}
