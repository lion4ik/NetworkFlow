package com.github.lion4ik.networkflow

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import com.github.lion4ik.networkflow.internet.InternetObservingStrategy
import com.github.lion4ik.networkflow.internet.SocketInternetObservingStrategy
import com.github.lion4ik.networkflow.networkstate.LollipopObservingStrategy
import com.github.lion4ik.networkflow.networkstate.NetworkObservingStrategy
import com.github.lion4ik.networkflow.networkstate.PreLollipopNetworkStateObservingStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.net.HttpURLConnection

class NetworkFlow(
    connectivityManager: ConnectivityManager,
    private val powerManager: PowerManager,
    private val internetObservingSettings: InternetObservingSettings = InternetObservingSettings()
) {

    internal companion object {
        internal const val LOG_TAG = "NetworkFlow"
    }

    constructor(
        appContext: Context,
        internetObservingSettings: InternetObservingSettings = InternetObservingSettings()
    ) : this(
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
        appContext.getSystemService(Context.POWER_SERVICE) as PowerManager,
        internetObservingSettings
    )

    @ExperimentalCoroutinesApi
    private val networkObservingStrategy: NetworkObservingStrategy = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> LollipopObservingStrategy(
            connectivityManager
        )
        else -> PreLollipopNetworkStateObservingStrategy(connectivityManager)
    }

    @ExperimentalCoroutinesApi
    fun observeNetworkState(appContext: Context): Flow<Connectivity> =
        networkObservingStrategy.observeNetworkState(appContext)

    @ExperimentalCoroutinesApi
    fun observeInternetConnectivity(): Flow<Boolean> = with(internetObservingSettings) {
        internetObservingStrategy.observeInternetConnectivity(
            interval,
            host, port, timeout, httpResponseCode,
            errorHandler
        )
    }

    class InternetObservingSettings(
        val internetObservingStrategy: InternetObservingStrategy = SocketInternetObservingStrategy(),
        val interval: Long = DEFAULT_INTERVAL,
        val host: String = DEFAULT_HOST,
        val port: Int = DEFAULT_PORT,
        val timeout: Int = DEFAULT_TIMEOUT,
        val httpResponseCode: Int = DEFAULT_HTTP_RESPONSE_CODE,
        val errorHandler: ErrorHandler = DefaultErrorHandler()
    ) {
        companion object {
            private const val DEFAULT_INTERVAL = 2000L
            private const val DEFAULT_HOST = "www.google.com"
            private const val DEFAULT_PORT = 80
            private const val DEFAULT_TIMEOUT = 2000
            private const val DEFAULT_HTTP_RESPONSE_CODE = HttpURLConnection.HTTP_NO_CONTENT

            inline fun create(settingsBuilder: InternetObservingSettings.() -> Unit): InternetObservingSettings =
                InternetObservingSettings().apply(settingsBuilder)
        }
    }
}