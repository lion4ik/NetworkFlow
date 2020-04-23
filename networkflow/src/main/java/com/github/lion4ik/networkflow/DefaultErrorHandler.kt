package com.github.lion4ik.networkflow

import android.util.Log
import com.github.lion4ik.networkflow.NetworkFlow.Companion.LOG_TAG

class DefaultErrorHandler : ErrorHandler {

    override fun handleError(message: String, throwable: Throwable) {
        Log.e(LOG_TAG, message, throwable)
    }
}