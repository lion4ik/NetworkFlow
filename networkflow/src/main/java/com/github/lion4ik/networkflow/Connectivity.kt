package com.github.lion4ik.networkflow

import android.net.NetworkCapabilities
import android.net.NetworkInfo

data class Connectivity(
    val type: Int = UNKNOWN_TYPE,
    val typeName: String = "NONE"
) {

    enum class ConnectionType {
        WIFI,
        CELLULAR
    }

    companion object {
        const val UNKNOWN_TYPE = -1

        fun fromNetworkInfo(networkInfo: NetworkInfo?): Connectivity =
            if (networkInfo == null) {
                Connectivity()
            } else {
                Connectivity(networkInfo.type, networkInfo.typeName)
            }

        fun fromNetworkCapabilities(networkCapabilities: NetworkCapabilities): Connectivity = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                Connectivity(NetworkCapabilities.TRANSPORT_WIFI, "WIFI")
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                Connectivity(NetworkCapabilities.TRANSPORT_CELLULAR, "MOBILE")
            else -> Connectivity(UNKNOWN_TYPE, "NONE")
        }

        fun unavailable(): Connectivity = Connectivity(UNKNOWN_TYPE, "NONE")

    }
}