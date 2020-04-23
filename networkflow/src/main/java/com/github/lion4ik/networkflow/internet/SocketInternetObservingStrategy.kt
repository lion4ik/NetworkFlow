package com.github.lion4ik.networkflow.internet

import com.github.lion4ik.networkflow.ErrorHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.coroutines.coroutineContext

class SocketInternetObservingStrategy : InternetObservingStrategy {

    @ExperimentalCoroutinesApi
    override fun observeInternetConnectivity(
        intervalInMs: Long,
        host: String,
        port: Int,
        timeoutInMs: Int,
        httpResponse: Int,
        errorHandler: ErrorHandler
    ): Flow<Boolean> = flow {
        while (coroutineContext.isActive) {
            delay(intervalInMs)
            emit(isConnected(host, port, timeoutInMs, errorHandler))
        }
    }.catch { errorHandler.handleError("observing error", it) }.distinctUntilChanged()

    protected fun isConnected(
        host: String, port: Int,
        timeoutInMs: Int, errorHandler: ErrorHandler
    ): Boolean =
        with(Socket()) {
            try {
                connect(InetSocketAddress(host, port), timeoutInMs)
                isConnected
            } catch (e: IOException) {
                false
            } finally {
                try {
                    close()
                } catch (exception: IOException) {
                    errorHandler.handleError("Socket connection error", exception)
                }
            }
        }
}