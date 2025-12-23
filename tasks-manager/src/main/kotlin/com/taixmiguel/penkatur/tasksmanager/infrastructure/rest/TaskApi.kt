package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest

import com.taixmiguel.penkatur.tasksmanager.application.dto.TaskRequestDTO
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import jakarta.validation.Valid
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api/tasks")
interface TaskApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun findTasks(@BeanParam criteria: TaskSearchCriteria): List<Task>

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun saveTask(@Valid request: TaskRequestDTO): Response
}