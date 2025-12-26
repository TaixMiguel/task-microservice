package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest

import com.taixmiguel.penkatur.tasksmanager.application.TaskService
import com.taixmiguel.penkatur.tasksmanager.application.dto.TaskRequestDTO
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.infrastructure.rest.dto.TaskSearchCriteriaDTO
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.jwt.JsonWebToken

@ApplicationScoped
class TaskResource @Inject constructor(
    private val service: TaskService,
    private val jwt: JsonWebToken
): TaskApi {
    override fun findTasks(criteriaDTO: TaskSearchCriteriaDTO): List<Task> {
        val criteria = criteriaDTO.toTaskSearchCriteria()
        criteria.addUser(jwt.subject)
        return service.findTasks(criteria)
    }

    override fun saveTask(request: TaskRequestDTO): Response {
        val createdTask = service.saveTask(request.toTask(jwt.subject))
        return Response.status(Response.Status.CREATED).entity(createdTask).build()
    }
}