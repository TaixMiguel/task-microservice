package com.taixmiguel.penkatur.tasksmanager.application

import com.taixmiguel.penkatur.tasksmanager.application.port.TaskRepository
import com.taixmiguel.penkatur.tasksmanager.application.query.TaskSearchCriteria
import com.taixmiguel.penkatur.tasksmanager.domain.Task
import com.taixmiguel.penkatur.tasksmanager.domain.TaskType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant

@ExtendWith(MockitoExtension::class)
class TaskServiceTest {
    @Mock
    private lateinit var repository: TaskRepository

    @InjectMocks
    private lateinit var service: TaskService

    @Test
    fun shouldSaveTaskWhenIsValid() {
        val task = Task(title = "task-title", type = TaskType.WORK, dueDate = Instant.now())
        service.saveTask(task)
        Mockito.verify(repository).save(task)
    }

    @Test
    fun shouldFindTasks() {
        val criteria = TaskSearchCriteria()
        service.findTasks(criteria)
        Mockito.verify(repository).find(criteria)
    }
}