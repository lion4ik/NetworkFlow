# NetworkFlow
[![Build Status](https://travis-ci.org/lion4ik/NetworkFlow.svg?branch=master)](https://travis-ci.org/lion4ik/NetworkFlow)

NetworkFlow is a network state based on coroutine's flow. It's designed to listen network state changes. There are different strategies of listening: using socket with connecting to some host and android API which is based on network adapter state. 

### Observing network state

```kotlin
  val networkFlowChecker = NetworkFlow(appContext) // can be injected with any DI framework 
  viewModelScope.launch {
    networkFlowChecker.observeNetworkState(appContext)
        .collect {
            Log.d("DEBUG", "value = $it")
  }
```

It returns `Connectivity` to collector that stores set of connection type and roaming information.

### Observing internet connection through connect to host strategy

```kotlin
  val networkFlowChecker = NetworkFlow(appContext) // can be injected with any DI framework 
  viewModelScope.launch {
        networkFlowChecker.observeInternetConnectivity().flowOn(Dispatchers.IO).collect {
            Log.d("DEBUG", "is connected = $it")
        }
  }
```

It returns boolean to collector which indicates if connected or not connected to the internet.

### Note
This solution was inspired by https://github.com/pwittchen/ReactiveNetwork which is based on RxJava. The idea is to impmelent the same stuff based on coroutines.
