package com.taixmiguel.penkatur.tasksmanager.infrastructure.ai

import com.taixmiguel.penkatur.tasksmanager.application.TaskService
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import com.taixmiguel.penkatur.tasksmanager.infrastructure.ai.dto.AiToolResponse
import com.taixmiguel.penkatur.tasksmanager.infrastructure.ai.dto.TaskSearchCriteriaAiDTO
import io.quarkiverse.mcp.server.Tool
import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken

class TaskToolAdapter @Inject constructor(
    private val service: TaskService,
    private val jwt: JsonWebToken
) {
    @Authenticated
    @Tool(name = "findTasks", description = "Retrieves a list of tasks based on the provided search criteria, such as date range and task type. Filters are optional.")
    fun findTasks(criteria: TaskSearchCriteriaAiDTO): AiToolResponse<List<Task>> {
        val searchCriteria = criteria.toTaskSearchCriteria()
        searchCriteria.addUser(jwt.subject)
        return AiToolResponse(
            data = service.findTasks(searchCriteria),
            warnings = criteria.warnings.ifEmpty { null }
        )
    }

    @Tool(name = "availableTaskTypes", description = "Retrieves the list of available task types in the system.")
    fun availableTaskTypes(): List<String> {
        return TaskType.entries.map { it.name }
    }
}
