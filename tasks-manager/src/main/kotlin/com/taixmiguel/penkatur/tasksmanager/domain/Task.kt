package com.taixmiguel.penkatur.tasksmanager.domain

import java.time.Instant

data class Task(
    val id: String? = null,
    val title: String,
    val type: TaskType,
    val dueDate: Instant,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null)
