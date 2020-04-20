package com.github.lion4ik.networkflow.networkstate

import android.annotation.TargetApi
import android.content.Context
import android.net.*
import android.os.Build
import com.github.lion4ik.networkflow.Connectivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@ExperimentalCoroutinesApi
class LollipopObservingStrategy(private val connectivityManager: ConnectivityManager) :
    NetworkObservingStrategy {

    override fun observeNetworkState(appContext: Context): Flow<Connectivity> =
        channelFlow<Connectivity> {
            val connectivityCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    sendBlocking(Connectivity.fromNetworkCapabilities(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    if (connectivityManager.activeNetworkInfo == null) {
                        sendBlocking(Connectivity.unavailable())
                    }
                }
            }

            val request =
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                    .build()
            connectivityManager.registerNetworkCallback(request, connectivityCallback)
            awaitClose { connectivityManager.unregisterNetworkCallback(connectivityCallback) }
        }.distinctUntilChanged()
}