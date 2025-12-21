package com.taixmiguel.penkatur.tasksmanager.application.port

import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task

interface TaskRepository {
    fun save(task: Task): Task
    fun find(criteria: TaskSearchCriteria): List<Task>
}