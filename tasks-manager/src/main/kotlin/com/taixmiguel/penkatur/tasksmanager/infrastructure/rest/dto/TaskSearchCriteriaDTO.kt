package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest.dto

import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import jakarta.ws.rs.QueryParam
import java.time.Instant

data class TaskSearchCriteriaDTO(
    @field:QueryParam("dateFrom")
    val dateFrom: Instant? = null,

    @field:QueryParam("dateTo")
    val dateTo: Instant? = null,

    @field:QueryParam("type")
    val type: TaskType? = null
) {
    fun toTaskSearchCriteria(): TaskSearchCriteria {
        return TaskSearchCriteria(dateFrom = dateFrom, dateTo = dateTo, type= type)
    }
}
