package com.github.lion4ik.networkflow

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.PowerManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoroutinesApi
class NetworkFlow(
    private val connectivityManager: ConnectivityManager,
    private val powerManager: PowerManager
) {

    constructor(appContext: Context) : this(
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
        appContext.getSystemService(Context.POWER_SERVICE) as PowerManager
    )

    fun networkState(appContext: Context): Flow<Connectivity> = channelFlow<Connectivity> {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                sendBlocking(Connectivity.create(appContext))
            }
        }
        appContext.registerReceiver(receiver, filter)
        awaitClose { appContext.unregisterReceiver(receiver) }
    }.distinctUntilChanged()
}