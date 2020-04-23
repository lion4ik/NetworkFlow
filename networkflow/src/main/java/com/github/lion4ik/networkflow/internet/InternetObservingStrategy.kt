package com.github.lion4ik.networkflow.internet

import com.github.lion4ik.networkflow.ErrorHandler
import kotlinx.coroutines.flow.Flow

interface InternetObservingStrategy {

    fun observeInternetConnectivity(
        intervalInMs: Long, host: String, port: Int, timeoutInMs: Int,
        httpResponse: Int, errorHandler: ErrorHandler
    ): Flow<Boolean>
}