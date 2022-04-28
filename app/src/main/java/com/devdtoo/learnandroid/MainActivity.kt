package com.devdtoo.learnandroid

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.devdtoo.learnandroid.databinding.ActivityMainBinding
import com.devdtoo.learnandroid.services.LocalBoundService

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var mService: LocalBoundService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("BoundService", "onServiceCommand triggered")
            val binder = service as LocalBoundService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("BoundService", "onServieDisconnected triggered")
            mBound = false
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.randonBtn.setOnClickListener {
            Log.d("BoundService", "onButtonClick triggered")
            if (mBound){
                Log.d("BoundService", "onButtonClick validation $mBound")
                val randomNumber = mService.randomNumber
                Log.d("BoundService", "onButtonClick Randon Number $randomNumber")
                binding.randonTv.text = randomNumber.toString()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, LocalBoundService::class.java).also {intent ->
            Log.d("BoundService", "onStart bindService Triggered")
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("BoundService", "onStart bindService Triggered")
        unbindService(connection)
        mBound = false
    }


}