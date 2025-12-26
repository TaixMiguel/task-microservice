package com.taixmiguel.penkatur.tasksmanager.application.query

import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import java.time.Instant

data class TaskSearchCriteria(
    val dateFrom: Instant? = null,
    val dateTo: Instant? = null,
    val type: TaskType? = null,
    val users: MutableList<String> = mutableListOf()
) {
    fun addUser(userId: String) = users.add(userId)
}
