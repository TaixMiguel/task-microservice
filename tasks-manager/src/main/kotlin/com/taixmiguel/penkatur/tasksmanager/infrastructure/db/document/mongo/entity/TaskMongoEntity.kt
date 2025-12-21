package com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo.entity

import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

@MongoEntity(collection = "tasks")
data class TaskMongoEntity(
    @field:BsonId
    var id: ObjectId? = null,
    var title: String = "Unknown",
    var type: TaskType = TaskType.WORK,
    var dueDate: Instant = Instant.now(),
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)
