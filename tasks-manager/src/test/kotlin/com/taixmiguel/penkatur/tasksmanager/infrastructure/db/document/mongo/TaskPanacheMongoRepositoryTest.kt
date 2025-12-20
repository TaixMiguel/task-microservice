package com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo

import com.taixmiguel.penkatur.tasksmanager.application.port.qualifier.DocumentRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo.TaskPanacheMongoRepository
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

@QuarkusTest
class TaskPanacheMongoRepositoryTest {

    @Inject
    @DocumentRepository
    private lateinit var repository: TaskPanacheMongoRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun testSaveTask_ShouldPersistOneEntry() {
        val newTask = Task(title = "example task", type = TaskType.WORK, dueDate = Instant.now())

        val savedTask = repository.save(newTask)

        Assertions.assertEquals(1, repository.count())
        Assertions.assertNotNull(savedTask.id)
        Assertions.assertEquals(newTask.title, savedTask.title)
        Assertions.assertEquals(newTask.type, savedTask.type)
        Assertions.assertEquals(newTask.dueDate, savedTask.dueDate)
        Assertions.assertNotNull(savedTask.createdAt)
        Assertions.assertNotNull(savedTask.updatedAt)
    }

    @Test
    fun testFindTasks_ShouldFoundAllTasks() {
        repository.save(Task(title = "example 1 of task", type = TaskType.WORK, dueDate = Instant.now().truncatedTo(ChronoUnit.SECONDS)))
        repository.save(Task(title = "example 2 of task", type = TaskType.WORK, dueDate = Instant.now().truncatedTo(ChronoUnit.SECONDS)))
        repository.save(Task(title = "example 3 of task", type = TaskType.WORK, dueDate = Instant.now().truncatedTo(ChronoUnit.SECONDS)))

        val criteria = TaskSearchCriteria()
        val logs = repository.find(criteria)
        Assertions.assertEquals(3, logs.size)
    }

    @Test
    fun testFindTasks_FilterByDateRange() {
        repository.save(Task(title = "example 1 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:00:00.00Z")))
        repository.save(Task(title = "example 2 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:05:00.00Z")))

        findTasksByDateRange(0, Instant.parse("2024-01-01T00:00:00.00Z"), Instant.parse("2024-12-31T23:59:59.00Z"))
        findTasksByDateRange(1, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T00:05:00.00Z"))
        findTasksByDateRange(1, Instant.parse("2025-01-01T00:05:00.00Z"), Instant.parse("2025-01-01T00:10:00.00Z"))
        findTasksByDateRange(2, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T00:10:00.00Z"))
    }

    @Test
    fun testFindTasks_FilterByType() {
        repository.save(Task(title = "example 1 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:00:00.00Z")))
        repository.save(Task(title = "example 2 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:05:00.00Z")))

        findTasksByType(2, TaskType.WORK)
        findTasksByType(0, TaskType.HOUSEWORK)
    }

    @Test
    fun testFindTasks_FilterByDateRangeAndType() {
        repository.save(Task(title = "example 1 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:00:00.00Z")))
        repository.save(Task(title = "example 2 of task", type = TaskType.WORK, dueDate = Instant.parse("2025-01-01T00:05:00.00Z")))

        findTasksByDateRangeAndType(0, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T00:10:00.00Z"),
            TaskType.HOUSEWORK)
        findTasksByDateRangeAndType(2, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T00:10:00.00Z"),
            TaskType.WORK)
    }

    private fun findTasksByDateRange(expected: Int, dateFrom: Instant, dateTo: Instant) {
        val criteria = TaskSearchCriteria(dateFrom = dateFrom, dateTo = dateTo)
        findTasks(expected, criteria)
    }

    private fun findTasksByDateRangeAndType(expected: Int, dateFrom: Instant, dateTo: Instant, type: TaskType) {
        val criteria = TaskSearchCriteria(dateFrom = dateFrom, dateTo = dateTo, type = type)
        findTasks(expected, criteria)
    }

    private fun findTasksByType(expected: Int, type: TaskType) {
        val criteria = TaskSearchCriteria(type = type)
        findTasks(expected, criteria)
    }

    private fun findTasks(expected: Int, criteria: TaskSearchCriteria) {
        val logs = repository.find(criteria)
        Assertions.assertEquals(expected, logs.size)
    }
}