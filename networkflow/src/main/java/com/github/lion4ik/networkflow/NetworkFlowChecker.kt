package com.github.lion4ik.networkflow

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@ExperimentalCoroutinesApi
class NetworkFlowChecker {

    fun networkState(appContext: Context): Flow<Connectivity> = channelFlow {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                sendBlocking(Connectivity.create(appContext))
            }
        }
        appContext.registerReceiver(receiver, filter)
        awaitClose { appContext.unregisterReceiver(receiver) }
    }
}