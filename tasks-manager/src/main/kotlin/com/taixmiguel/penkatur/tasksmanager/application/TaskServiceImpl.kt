package com.taixmiguel.penkatur.tasksmanager.application

import com.taixmiguel.penkatur.tasksmanager.application.port.TaskRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class TaskServiceImpl @Inject constructor(
    private val repository: TaskRepository
): TaskService {
    override fun saveTask(task: Task): Task {
        if (task.userID.isBlank()) throw IllegalArgumentException("Task userID cannot be blank.")
        if (task.title.isBlank()) throw IllegalArgumentException("Task title cannot be blank.")
        return repository.save(task)
    }

    override fun findTasks(criteria: TaskSearchCriteria): List<Task> {
        if (criteria.dateFrom != null && criteria.dateTo != null && criteria.dateFrom.isAfter(criteria.dateTo))
            throw IllegalArgumentException("dateFrom cannot be after dateTo.")
        return repository.find(criteria)
    }
}