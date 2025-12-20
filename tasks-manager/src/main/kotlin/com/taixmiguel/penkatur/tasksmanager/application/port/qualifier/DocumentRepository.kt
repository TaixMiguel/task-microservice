package com.taixmiguel.penkatur.tasksmanager.application.port.qualifier

import jakarta.inject.Qualifier

import kotlin.annotation.Retention
import kotlin.annotation.Target

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class DocumentRepository