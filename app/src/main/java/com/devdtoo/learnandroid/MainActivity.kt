package com.devdtoo.learnandroid

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.devdtoo.learnandroid.databinding.ActivityMainBinding
import com.devdtoo.learnandroid.services.MusicPlayerService
import com.devdtoo.learnandroid.utils.Constants

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var musicService: MusicPlayerService
    private var mBound: Boolean = false



    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "onServiceCommand() triggered")
            val serviceBinder = binder as MusicPlayerService.ServiceBinder
            musicService = serviceBinder.getService()
            mBound = true
            if (musicService.isPlaying()) binding.playBtn.text = "Pause"
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected() triggered")
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(binding) {
            runBtn.setOnClickListener {
                Log.d("BoundService", "onButtonClick triggered")
                if (mBound){
                    makeItStartedService()
                }
            }
            playBtn.setOnClickListener {
                Log.d("BoundService", "onButtonClick triggered")
                if (mBound){
                    if (musicService.isPlaying()) musicService.apply {
                        pause()
                        playBtn.text = "Play"
                    }
                    else musicService.apply {
                        makeItStartedService()
                        play()
                        playBtn.text = "Pause"
                    }
                }
            }
            clearBtn.setOnClickListener {
                Log.d("BoundService", "onButtonClick triggered")
                if (mBound){

                }
            }
        }
        observe()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() bindService Triggered")
        startBoundService()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() bindService Triggered")
        if (mBound) {
            unbindService(serviceConnection)
            mBound = false
        }

    }

    private fun observe() {
        MusicPlayerService.musicState.observe(this@MainActivity) {
            it?.let {
                if (it) binding.playBtn.text = "Play"
            }
        }
    }

    private fun startBoundService() {
        Intent(this@MainActivity, MusicPlayerService::class.java).also {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }
    private fun makeItStartedService() {
        Intent(this@MainActivity, MusicPlayerService::class.java).also {
            it.action = Constants.MUSIC_SERVICE_ACTION_START
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(it)
            else startService(it)
        }
    }

}