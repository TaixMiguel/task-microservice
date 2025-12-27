package com.taixmiguel.penkatur.tasksmanager.infrastructure.ai.dto

data class AiToolResponse<T>(
    val data: T?,
    val errors: List<String>? = null,
    val warnings: List<String>? = null
)