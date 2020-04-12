package com.github.lion4ik.networkflow

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkInfo.DetailedState

data class Connectivity(
    val state: NetworkInfo.State = NetworkInfo.State.DISCONNECTED,
    val detailedState: DetailedState = DetailedState.IDLE,
    val type: Int = UNKNOWN_TYPE,
    val subType: Int = UNKNOWN_SUB_TYPE,
    val available: Boolean = false,
    val failover: Boolean = false,
    val roaming: Boolean = false,
    val typeName: String = "NONE",
    val subTypeName: String = "NONE",
    val reason: String = "",
    val extraInfo: String = ""
) {
    companion object {
        const val UNKNOWN_TYPE = -1
        const val UNKNOWN_SUB_TYPE = -1

        fun fromNetworkInfo(networkInfo: NetworkInfo) =
            Connectivity(
                networkInfo.state,
                networkInfo.detailedState,
                networkInfo.type,
                networkInfo.subtype,
                networkInfo.isAvailable,
                networkInfo.isFailover,
                networkInfo.isRoaming,
                networkInfo.typeName,
                networkInfo.subtypeName,
                networkInfo.reason ?: "",
                networkInfo.extraInfo ?: ""
            )

        private fun getConnectivityManager(context: Context): ConnectivityManager {
            val service = Context.CONNECTIVITY_SERVICE
            return context.getSystemService(service) as ConnectivityManager
        }

        fun create(appContext: Context): Connectivity =
            create(getConnectivityManager(appContext))

        private fun create(
            manager: ConnectivityManager
        ): Connectivity {
            val networkInfo = manager.activeNetworkInfo
            return if (networkInfo == null) Connectivity() else fromNetworkInfo(networkInfo)
        }

    }


}