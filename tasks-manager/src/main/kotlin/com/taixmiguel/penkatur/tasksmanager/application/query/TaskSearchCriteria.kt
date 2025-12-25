package com.taixmiguel.penkatur.tasksmanager.application.query

import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import jakarta.ws.rs.QueryParam
import java.time.Instant

data class TaskSearchCriteria(
    @field:QueryParam("dateFrom")
    val dateFrom: Instant? = null,
    @field:QueryParam("dateTo")
    val dateTo: Instant? = null,
    @field:QueryParam("type")
    val type: TaskType? = null,
    val users: MutableList<String> = mutableListOf()
) {
    fun addUser(userId: String) = users.add(userId)
}
