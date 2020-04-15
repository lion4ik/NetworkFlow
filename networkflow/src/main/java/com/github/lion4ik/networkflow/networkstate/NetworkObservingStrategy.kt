package com.github.lion4ik.networkflow.networkstate

import android.content.Context
import com.github.lion4ik.networkflow.Connectivity
import kotlinx.coroutines.flow.Flow

interface NetworkObservingStrategy {

    fun observeNetworkState(appContext: Context): Flow<Connectivity>
}