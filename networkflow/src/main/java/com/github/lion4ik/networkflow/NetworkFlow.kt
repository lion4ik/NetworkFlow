package com.github.lion4ik.networkflow

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import com.github.lion4ik.networkflow.networkstate.LollipopObservingStrategy
import com.github.lion4ik.networkflow.networkstate.NetworkObservingStrategy
import com.github.lion4ik.networkflow.networkstate.PreLollipopNetworkStateObservingStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class NetworkFlow(
    connectivityManager: ConnectivityManager,
    private val powerManager: PowerManager
) {

    constructor(appContext: Context) : this(
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
        appContext.getSystemService(Context.POWER_SERVICE) as PowerManager
    )


    private val networkObservingStrategy: NetworkObservingStrategy = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> LollipopObservingStrategy(connectivityManager)
        else -> PreLollipopNetworkStateObservingStrategy(connectivityManager)
    }

    fun networkState(appContext: Context): Flow<Connectivity> =
        networkObservingStrategy.observeNetworkState(appContext)
}