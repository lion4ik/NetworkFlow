package com.github.lion4ik.networkflow

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

data class Connectivity(
    val type: Set<ConnectionType> = setOf(ConnectionType.NONE),
    val isRoaming: Boolean = false
) {

    fun hasAnyNetwork(): Boolean =
        type.any { it != ConnectionType.NONE }

    enum class ConnectionType {
        NONE,
        WIFI,
        CELLULAR,
        ETHERNET
    }

    companion object {

        @SuppressWarnings("deprecation")
        private fun fromOldConnectionType(networkInfo: NetworkInfo): ConnectionType =
            when (networkInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> ConnectionType.CELLULAR
                ConnectivityManager.TYPE_WIFI -> ConnectionType.WIFI
                ConnectivityManager.TYPE_ETHERNET -> ConnectionType.ETHERNET
                else -> ConnectionType.NONE
            }

        @SuppressWarnings("deprecation")
        fun fromNetworkInfo(networkInfo: NetworkInfo?): Connectivity =
            if (networkInfo == null) {
                Connectivity()
            } else {
                Connectivity(setOf(fromOldConnectionType(networkInfo)), networkInfo.isRoaming)
            }

        fun fromNetworkCapabilities(networkCapabilities: NetworkCapabilities, networkInfo: NetworkInfo?): Connectivity {
            val networkTypes = mutableSetOf<ConnectionType>()
            networkCapabilities.takeIf { it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) }?.run { networkTypes.add(ConnectionType.CELLULAR) }
            networkCapabilities.takeIf { it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) }?.run { networkTypes.add(ConnectionType.WIFI) }
            networkCapabilities.takeIf { it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) }?.run { networkTypes.add(ConnectionType.ETHERNET) }
            networkTypes.takeIf { it.isEmpty() }?.run { networkTypes.add(ConnectionType.NONE) }
            val isRoaming = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
            } else {
                networkInfo?.isRoaming ?: false
            }
            return Connectivity(networkTypes, isRoaming)
        }

        fun unavailable(): Connectivity = Connectivity()

    }
}