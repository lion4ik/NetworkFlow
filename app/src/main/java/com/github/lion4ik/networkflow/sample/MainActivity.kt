package com.github.lion4ik.networkflow.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.setOnClickListener {
            mainViewModel.startNetworkObserving(applicationContext)
//            mainViewModel.startInternetObserving(applicationContext)
        }
    }
}