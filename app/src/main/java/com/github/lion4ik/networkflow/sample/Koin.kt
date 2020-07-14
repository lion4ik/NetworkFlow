package com.github.lion4ik.networkflow.sample

import com.github.lion4ik.networkflow.NetworkFlow
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single { NetworkFlow(androidApplication()) }
    viewModel { MainViewModel(get()) }
}