package com.taixmiguel.penkatur.tasksmanager.application

import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task

interface TaskService {
    fun saveTask(task: Task): Task
    fun findTasks(criteria: TaskSearchCriteria): List<Task>
}