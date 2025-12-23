package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

import io.restassured.RestAssured.given
import io.restassured.RestAssured.`when`
import java.time.Instant

@QuarkusTest
class TaskResourceIntegrationTest {
    @Test
    fun testGetTasksReturnsStatus200() {
        `when`()
            .get("/api/tasks")
        .then()
            .statusCode(200)
    }

    @Test
    fun testCreateTaskReturnsStatus201WhenMessageIsValid() {
        val bodyJson = """
            { "title": "Test task", "type": "MEAL", "dueDate": "${Instant.now().toString()}" }
        """

        given()
            .contentType(ContentType.JSON)
            .body(bodyJson)
        .`when`()
            .post("/api/tasks")
        .then()
            .statusCode(201)
    }

    @Test
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingDueDate() {
        val bodyJson = """
            { "title": "Test task", "type": "MEAL" }
        """

        given()
            .contentType(ContentType.JSON)
            .body(bodyJson)
        .`when`()
            .post("/api/tasks")
        .then()
            .statusCode(400)
    }

    @Test
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingType() {
        val bodyJson = """
            { "title": "Test task", "dueDate": "${Instant.now().toString()}" }
        """

        given()
            .contentType(ContentType.JSON)
            .body(bodyJson)
        .`when`()
            .post("/api/tasks")
        .then()
            .statusCode(400)
    }

    @Test
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingTitle() {
        val bodyJson = """
            { "title": "type": "MEAL", "dueDate": "${Instant.now().toString()}" }
        """

        given()
            .contentType(ContentType.JSON)
            .body(bodyJson)
        .`when`()
            .post("/api/tasks")
        .then()
            .statusCode(400)
    }
}