package com.github.lion4ik.networkflow

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.PowerManager
import com.github.lion4ik.networkflow.networkstate.LollipopNetworkStateObservingStrategy
import com.github.lion4ik.networkflow.networkstate.MarshmallowNetworkStateObservingStrategy
import com.github.lion4ik.networkflow.networkstate.NetworkRequestFactory
import com.github.lion4ik.networkflow.networkstate.PreLollipopNetworkStateObservingStrategy
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NetworkStateObservingStrategiesTest {

    @MockK
    private lateinit var connectivityManager: ConnectivityManager

    @MockK
    private lateinit var powerManager: PowerManager

    @MockK
    private lateinit var appContext: Context

    @MockK
    private lateinit var networkCapabilitiesFactory: NetworkRequestFactory

    @MockK
    private lateinit var intentFilter: IntentFilter

    @Before
    fun initTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        val networkInfo = mockk<NetworkInfo>()
        val activeNetwork = mockk<Network>()
        val intent = mockk<Intent>()
        val networkRequest = mockk<NetworkRequest>()
        every {
            connectivityManager.registerNetworkCallback(
                any(),
                any<ConnectivityManager.NetworkCallback>()
            )
        } returns Unit
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } returns Unit
        every { connectivityManager.activeNetworkInfo } returns networkInfo
        every { connectivityManager.activeNetwork } returns activeNetwork
        every {
            networkCapabilitiesFactory.createNetworkRequest(
                any(),
                any()
            )
        } returns networkRequest
        every { appContext.registerReceiver(any(), any()) } returns intent
        every { appContext.unregisterReceiver(any()) } returns Unit
    }

    @Test
    fun `test PreLollipopNetworkStateObservingStrategy check receiver registered and unregistered called when cancel coroutine scope`() =
        runBlocking {
            val preLollipopNetworkStateObservingStrategy =
                PreLollipopNetworkStateObservingStrategy(connectivityManager, intentFilter)

            val deferred = async {

                preLollipopNetworkStateObservingStrategy.observeNetworkState(appContext).collect {

                }
            }
            delay(100)
            deferred.cancelAndJoin()
            verify {
                appContext.registerReceiver(any(), any())
                appContext.unregisterReceiver(any())
            }
        }

    @Test
    fun `test LollipopNetworkStateObservingStrategy check registerNetworkCallback and unregisterNetworkCallback called when cancel coroutine scope`() =
        runBlocking {
            val lollipopNetworkStateObservingStrategy =
                LollipopNetworkStateObservingStrategy(
                    connectivityManager,
                    networkCapabilitiesFactory
                )
            val deferred = async {

                lollipopNetworkStateObservingStrategy.observeNetworkState(appContext).collect {

                }
            }
            delay(100)
            deferred.cancelAndJoin()
            verify {
                connectivityManager.registerNetworkCallback(
                    any(),
                    any<ConnectivityManager.NetworkCallback>()
                )
                connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>())
            }
        }

    @Test
    fun `test MarshmallowNetworkStateObservingStrategy check register and unregister when cancel coroutine scope`() =
        runBlocking {
            val marshmallowNetworkStateObservingStrategy = MarshmallowNetworkStateObservingStrategy(
                connectivityManager,
                powerManager,
                networkCapabilitiesFactory,
                intentFilter
            )
            val deferred = async {

                marshmallowNetworkStateObservingStrategy.observeNetworkState(appContext).collect {

                }
            }
            delay(100)
            deferred.cancelAndJoin()
            verify {
                connectivityManager.registerNetworkCallback(
                    any(),
                    any<ConnectivityManager.NetworkCallback>()
                )
                connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>())
            }
        }
}