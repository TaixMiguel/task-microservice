package com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo

import com.mongodb.client.model.Filters
import com.taixmiguel.penkatur.tasksmanager.application.port.TaskRepository
import com.taixmiguel.penkatur.tasksmanager.application.port.qualifier.DocumentRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.mapper.toDomain
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.mapper.toEntityMongo
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo.entity.TaskMongoEntity
import io.quarkus.mongodb.panache.PanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.bson.conversions.Bson
import org.bson.types.ObjectId

@ApplicationScoped
@DocumentRepository
class TaskPanacheMongoRepository: TaskRepository, PanacheMongoRepository<TaskMongoEntity> {
    override fun save(task: Task): Task {
        val existingEntity = task.id
            ?.takeIf { ObjectId.isValid(it) }
            ?.let { findById(ObjectId(it)) }
        val entity = task.toEntityMongo(existingEntity)
        persistOrUpdate(entity)

        return entity.toDomain()
    }

    override fun find(criteria: TaskSearchCriteria): List<Task> {
        val filters = mutableListOf<Bson>()

        criteria.dateFrom?.let { from -> filters.add(Filters.gte("dueDate", from)) }
        criteria.dateTo?.let { to -> filters.add(Filters.lt("dueDate", to)) }
        criteria.type?.let { type -> filters.add(Filters.eq("type", type)) }

        val query = when {
            filters.isEmpty() -> null
            filters.size == 1 -> filters.first()
            else -> Filters.and(filters)
        }

        return (if (query == null) findAll() else find(query))
            .list<TaskMongoEntity>()
            .map(TaskMongoEntity::toDomain)
    }
}