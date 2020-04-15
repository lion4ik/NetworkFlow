package com.github.lion4ik.networkflow.networkstate

import android.content.Context
import com.github.lion4ik.networkflow.Connectivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@ExperimentalCoroutinesApi
class LollipopObservingStrategy: NetworkObservingStrategy {

    override fun observeNetworkState(appContext: Context): Flow<Connectivity> = channelFlow<Connectivity>{

    }
}