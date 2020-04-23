package com.github.lion4ik.networkflow

interface ErrorHandler {

    fun handleError(message: String, throwable: Throwable)
}