package com.taixmiguel.penkatur.tasksmanager.infrastructure.db

import com.taixmiguel.penkatur.tasksmanager.application.port.TaskRepository
import com.taixmiguel.penkatur.tasksmanager.application.port.qualifier.DocumentRepository
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo.TaskPanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Produces

@ApplicationScoped
class TaskRepositoryProducer {
    @Inject
    @DocumentRepository
    private lateinit var documentRepository: TaskPanacheMongoRepository

    @Produces
    @ApplicationScoped
    fun taskRepository(): TaskRepository {
        return documentRepository
    }
}