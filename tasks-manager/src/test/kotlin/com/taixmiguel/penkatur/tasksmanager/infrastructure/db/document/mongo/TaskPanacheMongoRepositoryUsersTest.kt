package com.taixmiguel.penkatur.tasksmanager.infrastructure.db.document.mongo

import com.taixmiguel.penkatur.tasksmanager.application.port.qualifier.DocumentRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

@QuarkusTest
class TaskPanacheMongoRepositoryUsersTest {

    @Inject
    @DocumentRepository
    private lateinit var repository: TaskPanacheMongoRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun testFindTasks_FilterByUsers() {
        repository.save(Task(title = "Task for User A", userID = "userA", type = TaskType.WORK, dueDate = Instant.now()))
        repository.save(Task(title = "Task for User B", userID = "userB", type = TaskType.WORK, dueDate = Instant.now()))
        repository.save(Task(title = "Task for User A again", userID = "userA", type = TaskType.WORK, dueDate = Instant.now()))

        // Test filtering by single user
        var criteria = TaskSearchCriteria()
        criteria.addUser("userA")
        var tasks = repository.find(criteria)
        Assertions.assertEquals(2, tasks.size, "Should find 2 tasks for userA")

        // Test filtering by multiple users
        criteria = TaskSearchCriteria()
        criteria.addUser("userA")
        criteria.addUser("userB")
        tasks = repository.find(criteria)
        Assertions.assertEquals(3, tasks.size, "Should find 3 tasks for userA and userB")

        // Test filtering by non-existent user
        criteria = TaskSearchCriteria()
        criteria.addUser("userC")
        tasks = repository.find(criteria)
        Assertions.assertEquals(0, tasks.size, "Should find 0 tasks for userC")
    }

    @Test
    fun testFindTasks_EmptyUsersList_ShouldReturnAll() {
        repository.save(Task(title = "Task for User A", userID = "userA", type = TaskType.WORK, dueDate = Instant.now()))
        repository.save(Task(title = "Task for User B", userID = "userB", type = TaskType.WORK, dueDate = Instant.now()))

        // Test empty users list (default)
        val criteria = TaskSearchCriteria() 
        // criteria.users is empty by default
        
        val tasks = repository.find(criteria)
        Assertions.assertEquals(2, tasks.size, "Should return all tasks when users list is empty")
    }
}
