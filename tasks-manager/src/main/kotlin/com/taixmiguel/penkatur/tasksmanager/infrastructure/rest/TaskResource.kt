package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest

import com.taixmiguel.penkatur.tasksmanager.application.TaskService
import com.taixmiguel.penkatur.tasksmanager.application.dto.TaskRequestDTO
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response

@ApplicationScoped
class TaskResource @Inject constructor(
    private val service: TaskService
): TaskApi {
    override fun findTasks(criteria: TaskSearchCriteria): List<Task> = service.findTasks(criteria)

    override fun saveTask(request: TaskRequestDTO): Response {
        val createdTask = service.saveTask(request.toTask())
        return Response.status(Response.Status.CREATED).entity(createdTask).build()
    }
}