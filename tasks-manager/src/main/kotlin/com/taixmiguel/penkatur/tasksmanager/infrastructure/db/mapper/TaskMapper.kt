package com.taixmiguel.penkatur.tasksmanager.infrastructure.db.mapper

import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo.entity.TaskMongoEntity
import org.bson.types.ObjectId
import java.time.Instant
import java.time.temporal.ChronoUnit

fun TaskMongoEntity.toDomain() = Task(
    id = this.id?.toString(),
    userID = this.userID,
    title = this.title,
    type = this.type,
    dueDate = this.dueDate,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun Task.toEntityMongo(existingEntity: TaskMongoEntity? = null) = TaskMongoEntity(
    type = this.type,
    title = this.title,
    userID = this.userID,
    dueDate = this.dueDate,
    updatedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS),
    createdAt = existingEntity?.createdAt ?: Instant.now().truncatedTo(ChronoUnit.SECONDS)
).apply {
    id =
        this@toEntityMongo.id
            ?.takeIf { ObjectId.isValid(it) }
            ?.let { ObjectId(it) }
            ?: existingEntity?.id
            ?: ObjectId()
}