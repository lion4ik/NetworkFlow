package com.github.lion4ik.networkflow.networkstate

import android.net.NetworkRequest

internal class NetworkRequestFactory {

    fun createNetworkRequest(vararg capability: Int): NetworkRequest =
        NetworkRequest.Builder().apply {
            capability.forEach {
                addCapability(it)
            }
        }.build()
}