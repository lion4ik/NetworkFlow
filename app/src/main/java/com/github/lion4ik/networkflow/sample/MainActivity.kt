package com.github.lion4ik.networkflow.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnNetworkStateObserve.setOnClickListener {
            mainViewModel.startNetworkObserving()
        }
        btnInternetStateObserver.setOnClickListener {
            mainViewModel.startInternetObserving()
        }
    }
}