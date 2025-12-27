package com.taixmiguel.penkatur.tasksmanager.infrastructure.ai.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import io.quarkus.runtime.annotations.RegisterForReflection
import org.jboss.logging.Logger
import org.jetbrains.annotations.Nullable
import java.time.Instant
import java.time.format.DateTimeParseException

@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TaskSearchCriteriaAiDTO(
    @field:JsonPropertyDescription("Start date for the search in ISO-8601 format. Optional.")
    @field:JsonProperty(required = false)
    @field:Nullable
    val dateFrom: String? = null,

    @field:JsonPropertyDescription("End date for the search in ISO-8601 format. Optional.")
    @field:JsonProperty(required = false)
    @field:Nullable
    val dateTo: String? = null,

    @field:JsonPropertyDescription("The type of task. Optional.")
    @field:JsonProperty(required = false)
    @field:Nullable
    val type: TaskType? = null,

    val warnings: MutableList<String> = mutableListOf()
) {
    fun toTaskSearchCriteria(): TaskSearchCriteria {
        return TaskSearchCriteria(
            dateFrom = dateFrom?.let { parseInstantSafely(it) },
            dateTo = dateTo?.let { parseInstantSafely(it) },
            type = type?.let { parseTypeSafely((it.name)) }
        )
    }

    private fun parseInstantSafely(date: String): Instant? {
        return try {
            Instant.parse(date)
        } catch (e: DateTimeParseException) {
            warnings.add("The date '$date' was invalid, ignoring it. Please use ISO-8601 format.")
            val log = Logger.getLogger(TaskSearchCriteriaAiDTO::class.java)
            log.warn("The LLM send an invalid date: $date.")
            null
        }
    }

    private fun parseTypeSafely(type: String): TaskType? {
        return try {
            TaskType.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            val types = TaskType.entries.joinToString(", ") { "'${it.name}'" }
            warnings.add("The type '$type' was invalid, ignoring it. Please use $types.")
            val log = Logger.getLogger(TaskSearchCriteriaAiDTO::class.java)
            log.warn("The LLM send an invalid task type: $type.")
            null
        }
    }
}
