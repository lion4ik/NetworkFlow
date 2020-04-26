package com.github.lion4ik.networkflow

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.github.lion4ik.networkflow.networkstate.PreLollipopNetworkStateObservingStrategy
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PreLollipopNetworkStateObservingStrategyTest {

    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val appContext: Context = mockk(relaxed = true)

    @Test
    fun test() {
        runBlocking {
            val preLollipopNetworkStateObservingStrategy =
                PreLollipopNetworkStateObservingStrategy(connectivityManager)

            preLollipopNetworkStateObservingStrategy.observeNetworkState(appContext).collect {

            }
        }

    }
}