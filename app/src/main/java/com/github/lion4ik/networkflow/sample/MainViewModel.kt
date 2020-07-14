package com.github.lion4ik.networkflow.sample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lion4ik.networkflow.NetworkFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val networkFlowChecker: NetworkFlow) : ViewModel() {

    fun startNetworkObserving() {
        viewModelScope.launch {
            networkFlowChecker.observeNetworkState()
                .collect {
                    Log.d("DEBUG", "value = $it")
                }
        }
    }

    fun startInternetObserving() {
        viewModelScope.launch {
            networkFlowChecker.observeInternetConnectivity().flowOn(Dispatchers.IO).collect {
                Log.d("DEBUG", "is connected = $it")
            }
        }
    }
}