package com.taixmiguel.penkatur.tasksmanager.infrastructure.ai

import com.taixmiguel.penkatur.tasksmanager.application.TaskService
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import com.taixmiguel.penkatur.tasksmanager.infrastructure.ai.dto.TaskSearchCriteriaAiDTO
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.Instant
import java.util.UUID

@QuarkusTest
@TestSecurity(user = "user-123")
class TaskToolAdapterTest {

    @Inject
    lateinit var taskToolAdapter: TaskToolAdapter

    @InjectMock
    lateinit var taskService: TaskService

    @InjectMock
    lateinit var jwt: JsonWebToken

    @Test
    fun `findTasks should return tasks when criteria is valid`() {
        val userId = "user-123"
        Mockito.`when`(jwt.subject).thenReturn(userId)

        val task = Task(
            id = UUID.randomUUID().toString(),
            title = "Test Task",
            type = TaskType.WORK,
            userID = userId,
            dueDate = Instant.now()
        )
        
        val dateFromStr = "2023-01-01T00:00:00Z"
        val criteriaDto = TaskSearchCriteriaAiDTO(
            dateFrom = dateFromStr,
            type = TaskType.WORK
        )

        val expectedCriteria = TaskSearchCriteria(
            dateFrom = Instant.parse(dateFromStr),
            type = TaskType.WORK
        )
        expectedCriteria.addUser(userId)

        Mockito.`when`(taskService.findTasks(expectedCriteria)).thenReturn(listOf(task))

        val response = taskToolAdapter.findTasks(criteriaDto)

        assertNotNull(response.data)
        assertEquals(1, response.data?.size)
        assertEquals(task, response.data?.first())
        Mockito.verify(taskService).findTasks(expectedCriteria)
    }

    @Test
    fun `findTasks should handle invalid date warnings`() {
        val userId = "user-123"
        Mockito.`when`(jwt.subject).thenReturn(userId)

        val criteriaDto = TaskSearchCriteriaAiDTO(
            dateFrom = "invalid-date",
            type = TaskType.WORK
        )
        
        // When date is invalid, it is ignored (null) in conversion, but added to warnings
        val expectedCriteria = TaskSearchCriteria(
            dateFrom = null,
            type = TaskType.WORK
        )
        expectedCriteria.addUser(userId)

        Mockito.`when`(taskService.findTasks(expectedCriteria)).thenReturn(emptyList())

        val response = taskToolAdapter.findTasks(criteriaDto)

        assertNotNull(response.data)
        assertEquals(0, response.data?.size)
        assertNotNull(response.warnings)
        assertTrue(response.warnings!!.any { it.contains("invalid-date") })
    }

    @Test
    fun `availableTaskTypes should return all task types`() {
        val types = taskToolAdapter.availableTaskTypes()
        
        assertEquals(TaskType.entries.size, types.size)
        TaskType.entries.forEach { 
            assertTrue(types.contains(it.name))
        }
    }
}
