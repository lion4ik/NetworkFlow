package com.github.lion4ik.networkflow.sample

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lion4ik.networkflow.NetworkFlowChecker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val networkFlowChecker = NetworkFlowChecker()

    fun startNetworkObserving(appContext: Context) {
        viewModelScope.launch {
            networkFlowChecker.networkState(appContext).collect {
                Log.d("DEBUG", "value = $it")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}