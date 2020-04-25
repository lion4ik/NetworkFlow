package com.github.lion4ik.networkflow.error

interface ErrorHandler {

    fun handleError(message: String, throwable: Throwable)
}