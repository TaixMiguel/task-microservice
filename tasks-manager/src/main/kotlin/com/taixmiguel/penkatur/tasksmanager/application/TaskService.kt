package com.taixmiguel.penkatur.tasksmanager.application

import com.taixmiguel.penkatur.tasksmanager.application.port.TaskRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class TaskService {
    @Inject
    private lateinit var repository: TaskRepository

    fun saveTask(task: Task): Task = repository.save(task)
    fun findTasks(criteria: TaskSearchCriteria): List<Task> = repository.find(criteria)
}