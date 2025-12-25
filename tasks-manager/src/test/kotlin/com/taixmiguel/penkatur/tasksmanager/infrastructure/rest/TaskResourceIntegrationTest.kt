package com.taixmiguel.penkatur.tasksmanager.infrastructure.rest

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.mockito.Mockito

import io.restassured.RestAssured.given
import io.restassured.RestAssured.`when`
import org.eclipse.microprofile.jwt.JsonWebToken
import java.time.Instant

@QuarkusTest
class TaskResourceIntegrationTest {

    @InjectMock
    private lateinit var jwt: JsonWebToken

    @Test
    @TestSecurity(user = "user-id-123")
    fun testGetTasksReturnsStatus200() {
        Mockito.`when`(jwt.subject).thenReturn("user-id-123")

        given().
        `when`()
            .get("/api/tasks")
        .then()
            .statusCode(200)
    }

    @Test
    fun testGetTasksReturnsStatus401() {
        `when`()
            .get("/api/tasks")
            .then()
            .statusCode(401)
    }

    @Test
    fun testCreateTaskReturnsStatus401WhenMessageIsValid() {
        val bodyJson = """
            { "title": "Test task", "type": "MEAL", "dueDate": "${Instant.now().toString()}" }
        """

        given()
            .contentType(ContentType.JSON)
            .body(bodyJson)
        .`when`()
            .post("/api/tasks")
        .then()
            .statusCode(401)
    }

    @Test
    @TestSecurity(user = "user-id-123")
    fun testCreateTaskReturnsStatus201WhenMessageIsValid() {
        Mockito.`when`(jwt.subject).thenReturn("user-id-123")
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
    @TestSecurity(user = "user-id-123")
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingDueDate() {
        Mockito.`when`(jwt.subject).thenReturn("user-id-123")
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
    @TestSecurity(user = "user-id-123")
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingType() {
        Mockito.`when`(jwt.subject).thenReturn("user-id-123")
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
    @TestSecurity(user = "user-id-123")
    fun testCreateTaskReturnsStatus400WhenMessageIsMissingTitle() {
        Mockito.`when`(jwt.subject).thenReturn("user-id-123")
        val bodyJson = """
            { "type": "MEAL", "dueDate": "${Instant.now().toString()}" }
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